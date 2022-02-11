package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 条件与
 *
 * @author liyifei <liyifei@kuaishou.com>
 */
public class AndNode implements RuleNode {

    private List<RuleNode> nodes = new ArrayList<>();

    public AndNode(RuleNode... nodes) {
        this.nodes.addAll(Arrays.asList(nodes));
    }

    public RuleNode and(RuleNode node) {
        this.nodes.add(node);
        return this;
    }

    public RuleNode or(RuleNode node) {
        return new OrNode(this, node);
    }

    public RuleNode not() {
        return new NotNode(this);
    }

    @Override
    public boolean matches(RuleContext ruleContext) {
        for (RuleNode node : nodes) {
            if (!node.matches(ruleContext)) {
                return false;
            }
        }
        return true;
    }
}
