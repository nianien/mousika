package com.skyfalling.mousika.eval.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.skyfalling.mousika.eval.RuleContext;

/**
 * 条件或
 *
 * @author liyifei <liyifei@kuaishou.com>
 */
public class OrNode implements RuleNode {

    private List<RuleNode> nodes = new ArrayList<>();

    public OrNode(RuleNode... nodes) {
        this.nodes.addAll(Arrays.asList(nodes));
    }

    public RuleNode or(RuleNode node) {
        this.nodes.add(node);
        return this;
    }

    public RuleNode and(RuleNode node) {
        return new AndNode(this, node);
    }

    public RuleNode not() {
        return new NotNode(this);
    }

    @Override
    public boolean matches(RuleContext context) {
        for (RuleNode node : nodes) {
            if (node.matches(context)) {
                return true;
            }
        }
        return false;
    }
}
