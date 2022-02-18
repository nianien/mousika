package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

/**
 * 条件取反
 *
 * @author liyifei 
 */
public class NotNode implements BoolNode {

    private BoolNode node;

    /**
     * 对node取反
     *
     * @param node
     */
    public NotNode(BoolNode node) {
        this.node = node;
    }

    @Override
    public BoolNode and(BoolNode o1) {
        return new AndNode(this, o1);
    }

    @Override
    public BoolNode or(BoolNode o1) {
        return new OrNode(this, o1);
    }

    @Override
    public BoolNode not() {
        return node;
    }


    @Override
    public boolean matches(RuleContext ruleContext) {
        return !node.matches(ruleContext);
    }
}
