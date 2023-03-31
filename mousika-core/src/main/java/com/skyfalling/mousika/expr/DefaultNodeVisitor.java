package com.skyfalling.mousika.expr;

import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.RuleContextImpl;
import com.skyfalling.mousika.eval.node.CompositeNode;
import com.skyfalling.mousika.eval.node.ExprNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.result.EvalResult;
import lombok.Data;

import java.util.List;
import java.util.Vector;

/**
 * 记录规则节点执行记录
 * Created on 2022/2/11
 *
 * @author liyifei
 */
public class DefaultNodeVisitor implements NodeVisitor {

    /**
     * 规则上下文
     */
    private RuleContext ruleContext;
    /**
     * 执行规则根节点
     */
    private EvalNode rootEval = new EvalNode(null);
    /**
     * 当前执行节点
     */
    private ThreadLocal<EvalNode> currentEval = ThreadLocal.withInitial(() -> rootEval);


    /**
     * 指定规则上下文
     *
     * @param ruleContext
     */
    public DefaultNodeVisitor(RuleContext ruleContext) {
        this.ruleContext = ruleContext;
    }

    /**
     * 访问规则节点
     */
    @Override
    public EvalResult visit(RuleNode node) {
        //只记录子节点
        if (node instanceof ExprNode) {
            EvalNode evalNode = new EvalNode(node.expr());
            currentEval.get().children.add(evalNode);
            if (node instanceof CompositeNode) {
                //更新当前节点为复合节点
                currentEval.set(evalNode);
            }
        }
        EvalResult result = node.eval(ruleContext);
        if (node instanceof CompositeNode) {//缓存复合节点的执行结果
            ((RuleContextImpl) ruleContext).getEvalCache().put(node.expr(), result);
            //复合节点执行完毕,回溯到父节点
            currentEval.set(currentEval.get().parent);
        }
        return result;
    }

    @Override
    public List<EvalNode> getEvalRules() {
        return rootEval.children;
    }


    @Override
    public void finish() {
        this.rootEval = new EvalNode(null);
    }


    /**
     * 评估节点信息
     */
    @Data
    public static class EvalNode {

        /**
         * 规则表达式
         */
        private final String expr;

        /**
         * 父执行节点
         */
        private EvalNode parent;

        /**
         * 子执行节点<br/>
         * 引擎支持并行,必须线程安全
         */
        private List<EvalNode> children = new Vector<>();

        /**
         * 执行节点
         *
         * @param expr
         */
        EvalNode(String expr) {
            this.expr = expr;
        }
    }

}