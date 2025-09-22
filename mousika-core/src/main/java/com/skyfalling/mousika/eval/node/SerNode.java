package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.context.RuleContext;
import com.skyfalling.mousika.eval.result.EvalResult;
import com.skyfalling.mousika.utils.Constants;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 顺序执行节点<br>
 * 最后一个节点的执行结果作为串行结果以及判断条件
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
@Getter
public class SerNode implements RuleNode {

    private List<RuleNode> nodes = new ArrayList<>();


    /**
     * @param nodes 子节点
     */
    public SerNode(RuleNode... nodes) {
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
        List<EvalResult> results = nodes.stream()
                .filter(e -> !e.expr().equals(Constants.NOP))
                .map(context::visit)
                .collect(Collectors.toList());
        EvalResult result = results.get(results.size() - 1);
        return new EvalResult(expr(), result.getResult(), result.isMatched());
    }


    @Override
    public String expr() {
        return String.join("->", nodes.stream()
                .map(Objects::toString/*RuleNode::expr*/)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "(" + expr() + ")";
    }
}
