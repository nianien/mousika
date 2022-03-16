package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 条件或
 *
 * @author liyifei
 */
public class OrNode implements RuleNode {

    private List<RuleNode> nodes = new ArrayList<>();

    /**
     * 多个节点条件取或
     *
     * @param nodes
     */
    public OrNode(RuleNode... nodes) {
        this.nodes.addAll(Arrays.asList(nodes));
    }

    @Override
    public RuleNode or(RuleNode node) {
        this.nodes.add(node);
        return this;
    }

    @Override
    public RuleNode and(RuleNode node) {
        return new AndNode(this, node);
    }

    @Override
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

    public String expr() {
        return String.join("||", nodes.stream()
                .map(Objects::toString)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return nodes.size() > 1 ? "(" + expr() + ")" : "" + expr();
    }
}
