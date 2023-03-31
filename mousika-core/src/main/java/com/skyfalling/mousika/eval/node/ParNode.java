package com.skyfalling.mousika.eval.node;


import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.result.EvalResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 并行执行节点<br/>
 * 最后执行的节点结果作为并行结果; 如果存在节点结果为真，则并行结果作为判断条件为真,否则为假
 * Created on 2023/3/31
 *
 * @author liyifei
 */
public class ParNode implements RuleNode {

    private List<RuleNode> nodes = new ArrayList<>();

    /**
     * @param node  至少一个节点
     * @param nodes 后继节点
     */
    public ParNode(RuleNode node, RuleNode... nodes) {
        this.nodes.add(node);
        this.nodes.addAll(Arrays.asList(nodes));
    }


    /**
     * 顺序执行
     */
    public ParNode next(RuleNode node) {
        this.nodes.add(node);
        return this;
    }

    @Override
    public EvalResult eval(RuleContext context) {
        List<EvalResult> results = nodes.parallelStream().map(context::visit).collect(Collectors.toList());
        EvalResult result = results.stream().filter(EvalResult::isMatched).findAny().orElse(new EvalResult(null, null));
        return new EvalResult(expr(), result.getResult(), result.isMatched());
    }


    @Override
    public String expr() {
        return String.join("=>", nodes.stream()
                .map(Objects::toString)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return nodes.size() > 1 ? "(" + expr() + ")" : expr();
    }
}