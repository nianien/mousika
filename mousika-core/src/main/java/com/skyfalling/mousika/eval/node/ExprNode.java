package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;
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
    public RuleNode and(RuleNode node) {
        return new AndNode(this, node);
    }

    @Override
    public RuleNode or(RuleNode node) {
        return new OrNode(this, node);
    }

    @Override
    public RuleNode not() {
        return new NotNode(this);
    }

    @Override
    public boolean matches(RuleContext context) {
        return context.eval(expression).isMatched();
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
