package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleVisitor;
import com.skyfalling.mousika.eval.result.EvalResult;
import lombok.Getter;

/**
 * 条件取反
 *
 * @author liyifei
 */
@Getter
public class NotNode implements RuleNode {

    private RuleNode node;

    /**
     * 对node取反
     */
    public NotNode(RuleNode node) {
        this.node = node;
    }


    @Override
    public RuleNode not() {
        return node;
    }


    @Override
    public EvalResult eval(RuleVisitor context) {
        EvalResult result = context.visit(node);
        return new EvalResult(expr(), !result.isMatched());
    }

    public String expr() {
        return "!" + node.toString();
    }

    @Override
    public String toString() {
        return this.expr();
    }
}
