package com.skyfalling.mousika.suite;

import lombok.Data;

/**
 * 规则评估异常
 *
 * @author liyifei <liyifei@kuaishou.com>
 * Created on 2021-11-19
 */
@Data
public class RuleEvalException extends RuntimeException {
    private String ruleId;

    public RuleEvalException(String ruleId, String message, Throwable e) {
        super(message, e);
        this.ruleId = ruleId;
    }

    public RuleEvalException(String ruleId, String message) {
        super(message);
        this.ruleId = ruleId;
    }
}
