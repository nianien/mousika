package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 条件与
 *
 * @author liyifei 
 */
public class AndNode implements BoolNode {

    private List<BoolNode> nodes = new ArrayList<>();

    /**
     * 多个节点条件取且
     *
     * @param nodes
     */
    public AndNode(BoolNode... nodes) {
        this.nodes.addAll(Arrays.asList(nodes));
    }

    @Override
    public BoolNode and(BoolNode node) {
        this.nodes.add(node);
        return this;
    }

    @Override
    public BoolNode or(BoolNode node) {
        return new OrNode(this, node);
    }

    @Override
    public BoolNode not() {
        return new NotNode(this);
    }

    @Override
    public boolean matches(RuleContext ruleContext) {
        for (BoolNode node : nodes) {
            if (!node.matches(ruleContext)) {
                return false;
            }
        }
        return true;
    }
}
