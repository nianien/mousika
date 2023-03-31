package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.result.EvalResult;
import lombok.Getter;

/**
 * 表达式规则
 *
 * @author liyifei
 */
@Getter
public class ExprNode implements RuleNode {

    /**
     * 规则表达式
     */
    private String expression;

    /**
     * 表达式节点
     *
     * @param expression 规则表达式
     */
    public ExprNode(String expression) {
        this.expression = expression;
    }

    @Override
    public EvalResult eval(RuleContext context) {
        return context.eval(expression);
    }

    @Override
    public String expr() {
        return expression;
    }

    @Override
    public String toString() {
        return expression;
    }
}
