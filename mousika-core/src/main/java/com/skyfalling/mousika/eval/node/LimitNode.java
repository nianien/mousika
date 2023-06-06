package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.context.RuleContext;
import com.skyfalling.mousika.eval.result.EvalResult;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 任意匹配节点
 */
@NoArgsConstructor
public class LimitNode implements RuleNode {

    /**
     * 最少匹配个数,-1表示不限
     */
    private int low;
    /**
     * 最多匹配个数,-1表示不限
     */
    private int high;
    /**
     * 待匹配节点数
     */
    private List<RuleNode> nodes = new ArrayList<>();

    public LimitNode(int low, int high, List<RuleNode> nodes) {
        this.low = low;
        this.high = high;
        this.nodes = nodes;
    }

    @Override
    public EvalResult eval(RuleContext context) {
        int hit = 0;
        EvalResult result = null;
        for (RuleNode node : nodes) {
            EvalResult eval = node.eval(context);
            if (result == null || !result.isMatched()) {
                result = eval;
            }
            if (eval.isMatched()) {
                result = eval;
                hit++;
            }
            if (high > 0 && hit > high) {
                break;
            }
        }
        return new EvalResult(expr(), result.getResult(), hit >= low && (high < 0 || hit <= high));
    }

    @Override
    public String expr() {
        return "limit(" + low + "," + high + "," + String.join(",", nodes.stream()
                .map(Objects::toString/*RuleNode::expr*/)
                .collect(Collectors.toList())) + ")";
    }

    @Override
    public String toString() {
        return this.expr();
    }
}
