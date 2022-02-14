package com.skyfalling.mousika.eval;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleResult {
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
                "ruleId=" + ruleId +
                ", matched=" + result.isMatched() +
                ", desc='" + desc + '\'' +
                ')';
    }
}