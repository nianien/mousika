package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.result.EvalResult;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 顺序执行节点<br/>
 * 最后一个节点的执行结果作为串行结果以及判断条件
 *
 * @author liyifei <liyifei@kuaishou.com>
 */
@Getter
public class SerNode implements RuleNode {

    private List<RuleNode> nodes = new ArrayList<>();

    /**
     * @param node  至少一个节点
     * @param nodes 后继节点
     */
    public SerNode(RuleNode node, RuleNode... nodes) {
        this.nodes.add(node);
        this.nodes.addAll(Arrays.asList(nodes));
    }


    /**
     * 顺序执行
     */
    public SerNode next(RuleNode node) {
        this.nodes.add(node);
        return this;
    }

    @Override
    public EvalResult eval(RuleContext context) {
        List<EvalResult> results = nodes.stream().map(context::visit).collect(Collectors.toList());
        EvalResult result = results.get(results.size() - 1);
        return new EvalResult(expr(), result.getResult(), result.isMatched());
    }


    @Override
    public String expr() {
        return String.join("->", nodes.stream()
                .map(Objects::toString)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return nodes.size() > 1 ? "(" + expr() + ")" : expr();
    }
}
