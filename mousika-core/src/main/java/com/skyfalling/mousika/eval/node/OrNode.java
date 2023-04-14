package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleVisitor;
import com.skyfalling.mousika.eval.result.EvalResult;
import lombok.Getter;

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
@Getter
public class OrNode implements RuleNode {

    private List<RuleNode> nodes = new ArrayList<>();

    /**
     * 多个节点条件取或
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
    public EvalResult eval(RuleVisitor context) {
        for (RuleNode node : nodes) {
            if (context.visit(node).isMatched()) {
                return new EvalResult(expr(), true);
            }
        }
        return new EvalResult(expr(), false);
    }

    public String expr() {
        return String.join("||", nodes.stream()
                .map(Objects::toString)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return nodes.size() > 1 ? "(" + expr() + ")" : expr();
    }
}
