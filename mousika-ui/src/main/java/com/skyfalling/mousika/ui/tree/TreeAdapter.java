package com.skyfalling.mousika.ui.tree;

import com.skyfalling.mousika.eval.node.*;
import com.skyfalling.mousika.eval.parser.NodeBuilder;
import com.skyfalling.mousika.ui.tree.node.*;
import com.skyfalling.mousika.ui.tree.node.define.*;

import java.util.List;

/**
 * UI树适配器,支持规则节点与UI节点的互相转换
 * Created on 2023/4/15
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
public class TreeAdapter {

    /**
     * 规则表达式转UI树
     */
    public static TreeNode fromRule(String rule) {
        return fromRule(NodeBuilder.build(rule));
    }


    /**
     * 规则转UI树
     */
    public static TreeNode fromRule(RuleNode rule) {
        TreeNode treeNode = new TreeNode();
        addFlow(rule, treeNode);
        return treeNode;
    }


    /**
     * UI树转换为规则
     */
    public static RuleNode toRule(TreeNode treeNode) {
        return new SerNode(treeNode.getFlows().stream().map(TreeAdapter::fromIFNode).toArray(n -> new RuleNode[n]));
    }


    /**
     * 添加流程节点
     *
     * @param ruleNode 规则
     * @param aNode    动作节点
     */
    private static void addFlow(RuleNode ruleNode, IANode aNode) {
        if (ruleNode instanceof ExprNode) {
            aNode.addFlow(FNode.of(ruleNode.expr()));
        } else if (ruleNode instanceof SerNode) {
            for (RuleNode rule : ((SerNode) ruleNode).getNodes()) {
                addFlow(rule, aNode);
            }
        } else if (ruleNode instanceof CaseNode) {
            ZNode zNode = new ZNode();
            toIPNode(ruleNode, zNode);
            aNode.addFlow(zNode);
        } else {
            throw unSupportException(ruleNode);
        }
    }


    /**
     * 规则转分支节点
     *
     * @param ruleNode
     * @param pNode
     * @return
     */
    private static IPNode toIPNode(RuleNode ruleNode, IPNode pNode) {
        if (ruleNode instanceof CaseNode) {
            CaseNode caseNode = (CaseNode) ruleNode;
            //condition作为分支节
            IPNode branch = (IPNode) toIRNode(caseNode.getCondition(), true);
            pNode.addBranch(branch);
            //trueCase作为子分支
            if (caseNode.getTrueCase() != null) {
                toIPNode(caseNode.getTrueCase(), branch);
            }
            //falseCase,作为下一分支
            if (caseNode.getFalseCase() != null) {
                toIPNode(caseNode.getFalseCase(), pNode);
            }
        } else if (ruleNode instanceof ExprNode) {
            pNode.setAction(ANode.of(ruleNode.expr()));
        } else {
            throw unSupportException(ruleNode);
        }
        return pNode;
    }

    /**
     * 规则转条件节点
     *
     * @param ruleNode 规则节点
     * @param isBranch 是否作为分支节点
     */
    private static IRNode toIRNode(RuleNode ruleNode, boolean isBranch) {
        boolean negative = false;
        if (ruleNode instanceof NotNode) {
            ruleNode = ruleNode.not();
            negative = true;
        }
        if (ruleNode instanceof ExprNode) {
            return isBranch ? PNode.of(ruleNode.expr(), negative) : RNode.of(ruleNode.expr(), negative);
        }
        ILNode node;
        if (ruleNode instanceof AndNode) {
            node = isBranch ? GNode.and() : LNode.and();
            node.setNegative(negative);
            ((AndNode) ruleNode).getNodes().forEach(r -> node.addRule(toIRNode(r, false)));
        } else if (ruleNode instanceof OrNode) {
            node = isBranch ? GNode.or() : LNode.or();
            node.setNegative(negative);
            ((OrNode) ruleNode).getNodes().forEach(r -> node.addRule(toIRNode(r, false)));
        } else {
            throw unSupportException(ruleNode);
        }
        return node;
    }

    /**
     * 流程节点转规则
     *
     * @param fNode
     * @return
     */
    private static RuleNode fromIFNode(IFNode fNode) {
        if (fNode instanceof FNode) {
            return NodeBuilder.build(fNode.ruleExpr());
        }
        if (fNode instanceof ZNode) {
            ZNode zNode = (ZNode<?>) fNode;
            CaseNode caseNode = fromIPNode(zNode);
            return caseNode.getTrueCase();
        } else {
            throw unSupportException(fNode);
        }
    }


    /**
     * 分支节点转规则
     *
     * @param pNode
     * @return
     */
    private static CaseNode fromIPNode(IPNode pNode) {
        //条件节点
        RuleNode condition = NodeBuilder.build(pNode.ruleExpr());
        //根节点
        CaseNode root = new CaseNode(condition, null);
        //第一个分支
        RuleNode head = null;
        //上一个分支
        CaseNode last = null;
        List<IPNode> branches = pNode.getBranches();
        for (IPNode branch : branches) {
            CaseNode caseNode = fromIPNode(branch);
            if (head == null) {
                head = caseNode;
            }
            //后面分支为前面分支的false case
            if (last != null) {
                last.setFalseCase(caseNode);
            }
            last = caseNode;
        }
        IANode aNode = pNode.getAction();
        if (aNode != null) {
            RuleNode action = NodeBuilder.build(aNode.originExpr());
            if (head == null) {
                head = action;
            }
            if (last != null) {
                last.setFalseCase(action);
            }
        }
        root.setTrueCase(head);
        return root;
    }

    private static RuntimeException unSupportException(RuleNode node) {
        return new UnsupportedOperationException("not support rule-Node type:" + node.getClass().getSimpleName());
    }

    private static RuntimeException unSupportException(TypeNode node) {
        return new UnsupportedOperationException("not support ui-Node type:" + node.getClass().getSimpleName());
    }
}
