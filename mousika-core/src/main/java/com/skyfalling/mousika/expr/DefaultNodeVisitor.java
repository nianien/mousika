package com.skyfalling.mousika.expr;

import com.skyfalling.mousika.eval.EvalResult;
import com.skyfalling.mousika.eval.NodeWrapper;
import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.node.ExprNode;
import com.skyfalling.mousika.eval.node.NotNode;
import com.skyfalling.mousika.eval.node.OrNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录规则节点执行记录
 * Created on 2022/2/11
 *
 * @author liyifei
 */
public class DefaultNodeVisitor implements NodeVisitor {

    private RuleContext ruleContext;
    /**
     * flag为true表示保留执行失败的规则,否则保留执行成功的规则
     */
    private boolean visitFlag = true;
    /**
     * 导致结果通过的规则集合
     */
    private List<String> trueRules = new ArrayList<>();
    /**
     * 导致结果未通过的规则集合
     */
    private List<String> falseRules = new ArrayList<>();

    /**
     * 影响结果的规则集合
     */
    @Getter
    private List<String> effectiveRules = new ArrayList<>();


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
     *
     * @param node
     * @return
     */
    @Override
    public EvalResult visit(RuleNode node) {
        //标记节点
        boolean flag = mark(node);
        int falseSize = falseRules.size();
        if (node instanceof NodeWrapper) {
            node = ((NodeWrapper) node).unwrap();
        }
        EvalResult result;
        if (node instanceof ExprNode) {
            result = ruleContext.eval(((ExprNode) node).getExpression());
        } else {
            result = new EvalResult(node.matches(ruleContext));
        }
        boolean matched = result.isMatched();
        if (node instanceof ExprNode) {
            if (matched && flag || !matched && !flag) {
                trueRules.add(((ExprNode) node).getExpression());
            } else {
                falseRules.add(((ExprNode) node).getExpression());
            }
        } else if (node instanceof OrNode) {
            //对于or节点,如果匹配结果为真,则移除fail的节点
            if (matched && flag || !matched && !flag) {
                falseRules = falseRules.subList(0, falseSize);
            }
        }
        //还原标记
        mark(node);
        return result;
    }

    @Override
    public void reset(int flag) {
        if (flag == 1) {
            this.effectiveRules.addAll(trueRules);
        } else if (flag == 0) {
            this.effectiveRules.addAll(falseRules);
        }
        this.trueRules.clear();
        this.falseRules.clear();
        this.visitFlag = true;
    }


    /**
     * 检验是否选择成功节点
     *
     * @param node
     * @return
     */
    private boolean mark(RuleNode node) {
        if (node instanceof NotNode) {
            visitFlag = !visitFlag;
        }
        return visitFlag;
    }


}
