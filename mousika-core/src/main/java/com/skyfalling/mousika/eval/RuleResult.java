package com.skyfalling.mousika.eval;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单个规则执行结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleResult {
    /**
     * 规则表达式
     */
    private String expr;
    /**
     * 规则ID
     */
    private String ruleId;
    /**
     * 规则执行结果
     */
    private EvalResult result;
    /**
     * 规则描述
     */
    private String desc;

    @Override
    public String toString() {
        return "RuleResult(" +
                "expr=\"" + expr +
                "\",ruleId=" + ruleId +
                ", matched=" + result.isMatched() +
                ", desc='" + desc + '\'' +
                ')';
    }
}