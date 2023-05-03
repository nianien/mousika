package com.skyfalling.mousika.ui.tree2;

import com.google.common.base.Preconditions;
import com.skyfalling.mousika.eval.node.*;
import com.skyfalling.mousika.eval.parser.NodeBuilder;
import com.skyfalling.mousika.ui.tree2.node.define.FlowNode;
import com.skyfalling.mousika.ui.tree2.node.flow.*;
import com.skyfalling.mousika.ui.tree2.node.rule.LNode;
import com.skyfalling.mousika.ui.tree2.node.rule.RNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2023/5/2
 *
 * @author liyifei
 */
public class UINodeAdapter {


    /**
     * UI节点转规则
     *
     * @param un
     * @return
     */
    public RuleNode toRule(FlowNode un) {
        Preconditions.checkNotNull(un, "ui node cannot be null!");
        if (un instanceof ANode) {
            ANode an = (ANode) un;
            return aNode2Rule(an, new ArrayList<>());
        }
        if (un instanceof CNode) {/*include JNode*/
            return cNode2Rule((CNode) un);
        }
        if (un instanceof DNode) {
            return dNode2Rule((DNode) un);
        }
        if (un instanceof SNode) {
            return sNode2Rule((SNode) un);
        }
        if (un instanceof PNode) {
            return pNode2Rule((PNode) un);
        }
        throw new UnsupportedOperationException("not support node type:" + un.getClass().getSimpleName());
    }

    /**
     * 规则转UI节点
     *
     * @param ruleNode
     * @return
     */
    public FlowNode fromRule(RuleNode ruleNode) {
        Preconditions.checkNotNull(ruleNode, "rule node cannot be null!");
        if (ruleNode instanceof CaseNode) {
            CaseNode caseNode = (CaseNode) ruleNode;
            if (caseNode.getFalseCase() == null) {
                return caseNode2CNode(caseNode);
            }
            return caseNode2DNode(caseNode, new DNode());
        }
        if (ruleNode instanceof SerNode) {
            return serNode2FlowNode((SerNode) ruleNode);
        }
        if (ruleNode instanceof ParNode) {
            return parNode2FlowNode((ParNode) ruleNode);
        }
        if (ruleNode instanceof NotNode) {
            CNode cn = new CNode(ruleNode.not().expr());
            cn.setNegative(true);
            return cn;
        }
        if (ruleNode instanceof AndNode || ruleNode instanceof OrNode) {
            JNode jn = new JNode();
            jn.setRule(ruleNode2RNode(ruleNode));
            return jn;
        }
        if (ruleNode instanceof ExprNode) {
            return new ANode(ruleNode.expr());
        }
        throw new UnsupportedOperationException("not support rule type:" + ruleNode.getClass().getSimpleName());
    }


    /**
     * PNode转Rule
     *
     * @param pn
     * @return
     */
    private RuleNode pNode2Rule(PNode pn) {
        List<RuleNode> rules = pn.getBranches().stream().map(this::toRule).collect(Collectors.toList());
        rules.add(0, new ExprNode(FlowNode.NOP));
        return new ParNode(rules.toArray(new RuleNode[0]));
    }

    /**
     * SNode转Rule
     *
     * @param sn
     * @return
     */
    private RuleNode sNode2Rule(SNode sn) {
        List<RuleNode> rules = sn.getBranches().stream().map(this::toRule).collect(Collectors.toList());
        rules.add(0, new ExprNode(FlowNode.NOP));
        return new SerNode(rules.toArray(new RuleNode[0]));
    }

    /**
     * CNode转Rule
     *
     * @param cn
     * @return
     */
    private RuleNode cNode2Rule(CNode cn) {
        if (cn.getAction() == null) {
            throw new IllegalArgumentException("CNode's action is required!");
        }
        return new CaseNode(NodeBuilder.build(cn.ruleExpr()), toRule(cn.getAction()));
    }


    /**
     * DNode转Rule
     *
     * @param dn
     * @return
     */
    private RuleNode dNode2Rule(DNode dn) {
        if (dn.getAction() == null) {
            throw new IllegalArgumentException("DNode's action is required!");
        }
        List<CNode> branches = new ArrayList<>(dn.getBranches());
        CaseNode root = new CaseNode(new ExprNode(""), new ExprNode(""));
        CaseNode current = root;
        for (int i = 0; i < branches.size(); i++) {
            CNode fn = branches.get(i);
            CaseNode caseNode = current;
            caseNode.setFalseCase(toRule(fn));
            current = (CaseNode) caseNode.getFalseCase();
        }
        if (dn.getAction() != null) {
            current.setFalseCase(toRule(dn.getAction()));
        }
        return root.getFalseCase();
    }

    /**
     * ANode转Rule
     *
     * @param an
     * @param nodes
     * @return
     */
    private RuleNode aNode2Rule(ANode an, List<RuleNode> nodes) {
        nodes.add(NodeBuilder.build(an.getExpr()));
        FlowNode next = an.getNext();
        if (next != null) {
            if (next instanceof ANode) {
                return aNode2Rule((ANode) next, nodes);
            } else {
                nodes.add(toRule(next));
            }
        }
        return nodes.size() == 1 ? nodes.get(0) : new SerNode(nodes.toArray(new RuleNode[0]));
    }


    /**
     * CaseNode转CNode
     *
     * @param caseNode
     * @return
     */
    private CNode caseNode2CNode(CaseNode caseNode) {
        RuleNode condition = caseNode.getCondition();
        RNode rn = ruleNode2RNode(condition);
        if (rn instanceof LNode) {
            JNode jn = new JNode();
            jn.setRule(rn);
            jn.setAction(fromRule(caseNode.getTrueCase()));
            return jn;
        } else {
            CNode cn = new CNode(rn.getExpr());
            cn.setNegative(rn.isNegative());
            cn.setAction(fromRule(caseNode.getTrueCase()));
            return cn;
        }
    }

    /**
     * CaseNode转DNode
     *
     * @param caseNode
     * @param dNode
     * @return
     */
    private DNode caseNode2DNode(CaseNode caseNode, DNode dNode) {
        dNode.addBranch(caseNode2CNode(caseNode));
        RuleNode falseCase = caseNode.getFalseCase();
        if (falseCase instanceof CaseNode) {
            caseNode2DNode((CaseNode) falseCase, dNode);
        } else if (falseCase != null) {
            dNode.setAction(fromRule(falseCase));
        }
        return dNode;
    }


    /**
     * RuleNode转RNode
     *
     * @param ruleNode 规则节点
     */
    private RNode ruleNode2RNode(RuleNode ruleNode) {
        Preconditions.checkNotNull(ruleNode, "rule node cannot be null!");
        boolean negative = false;
        if (ruleNode instanceof NotNode) {
            ruleNode = ruleNode.not();
            negative = true;
        }
        LNode node;
        if (ruleNode instanceof AndNode) {
            node = LNode.and();
            node.setNegative(negative);
            ((AndNode) ruleNode).getNodes().forEach(r -> node.addRule(ruleNode2RNode(r)));
        } else if (ruleNode instanceof OrNode) {
            node = LNode.or();
            node.setNegative(negative);
            ((OrNode) ruleNode).getNodes().forEach(r -> node.addRule(ruleNode2RNode(r)));
        } else {
            return new RNode(ruleNode.expr(), negative);
        }
        return node;
    }

    /**
     * SerNode转UINode
     *
     * @param serNode
     * @return
     */
    private FlowNode serNode2FlowNode(SerNode serNode) {
        List<RuleNode> nodes = serNode.getNodes();
        RuleNode first = nodes.get(0);
        nodes = nodes.subList(1, nodes.size());
        if (first.expr().equals(FlowNode.NOP)) {// SNode
            SNode sn = new SNode();
            nodes.forEach(n -> sn.addBranch(fromRule(n)));
            return sn;
        } else {//ANode
            ANode root = (ANode) fromRule(first);
            FlowNode current = root;
            for (RuleNode node : nodes) {
                ANode an = ((ANode) current);
                an.setNext(fromRule(node));
                current = an.getNext();
            }
            return root;
        }

    }


    /**
     * SerNode转UINode
     *
     * @param parNode
     * @return
     */
    private FlowNode parNode2FlowNode(ParNode parNode) {
        List<RuleNode> nodes = parNode.getNodes();
        RuleNode first = nodes.get(0);
        if (first.expr().equals(FlowNode.NOP)) {
            nodes = nodes.subList(1, nodes.size());
        }
        PNode pn = new PNode();
        nodes.forEach(n -> pn.addBranch(fromRule(n)));
        return pn;
    }

}
