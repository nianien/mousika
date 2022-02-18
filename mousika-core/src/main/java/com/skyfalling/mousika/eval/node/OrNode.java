package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 条件或
 *
 * @author liyifei 
 */
public class OrNode implements BoolNode {

    private List<BoolNode> nodes = new ArrayList<>();

    /**
     * 多个节点条件取或
     *
     * @param nodes
     */
    public OrNode(BoolNode... nodes) {
        this.nodes.addAll(Arrays.asList(nodes));
    }

    @Override
    public BoolNode or(BoolNode node) {
        this.nodes.add(node);
        return this;
    }

    @Override
    public BoolNode and(BoolNode node) {
        return new AndNode(this, node);
    }

    @Override
    public BoolNode not() {
        return new NotNode(this);
    }

    @Override
    public boolean matches(RuleContext context) {
        for (BoolNode node : nodes) {
            if (node.matches(context)) {
                return true;
            }
        }
        return false;
    }
}
