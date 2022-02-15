package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

/**
 * 条件取反
 *
 * @author liyifei <liyifei@kuaishou.com>
 */
public class NotNode implements RuleNode {

    private RuleNode node;

    /**
     * 对node取反
     *
     * @param node
     */
    public NotNode(RuleNode node) {
        this.node = node;
    }

    @Override
    public RuleNode and(RuleNode o1) {
        return new AndNode(this, o1);
    }

    @Override
    public RuleNode or(RuleNode o1) {
        return new OrNode(this, o1);
    }

    @Override
    public RuleNode not() {
        return node;
    }


    @Override
    public boolean matches(RuleContext ruleContext) {
        return !node.matches(ruleContext);
    }
}
