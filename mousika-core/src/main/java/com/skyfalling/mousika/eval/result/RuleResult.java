package com.skyfalling.mousika.eval.result;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则执行结果
 */
@Getter
public class RuleResult extends EvalResult {
    /**
     * @param result
     * @param desc
     */
    public RuleResult(EvalResult result, String desc) {
        super(result.getExpr(), result.getResult());
        this.desc = desc;
    }

    /**
     * 规则描述
     */
    private String desc;
    /**
     * 子规则
     */

    private List<RuleResult> details = new ArrayList<>();


    @Override
    public String toString() {
        return "RuleResult("
                + "ruleId=" + getExpr()
                + ",result=" + getResult()
                + (desc == null || desc.isEmpty() ? "" : ",desc='" + desc + '\'')
                + (details.isEmpty() ? "" : ",details=" + details)
                + ')';
    }
}
