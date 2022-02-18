package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 条件与
 *
 * @author liyifei
 */
public class AndNode implements RuleNode {

    private List<RuleNode> nodes = new ArrayList<>();

    /**
     * 多个节点条件取且
     *
     * @param nodes
     */
    public AndNode(RuleNode... nodes) {
        this.nodes.addAll(Arrays.asList(nodes));
    }

    @Override
    public RuleNode and(RuleNode node) {
        this.nodes.add(node);
        return this;
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
    public boolean matches(RuleContext ruleContext) {
        for (RuleNode node : nodes) {
            if (!node.matches(ruleContext)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public String toString() {
        return String.join("&&", nodes.stream()
                .map(Objects::toString)
                .map(s -> {
                    if (s.startsWith("(") && s.endsWith(")")) {
                        return s;
                    }
                    return "(" + s + ")";
                }).collect(Collectors.toList()));
    }
}
