package com.skyfalling.mousika.exception;

import lombok.Data;

/**
 * 规则评估异常
 *
 * @author liyifei
 * Created on 2021-11-19
 */
@Data
public class RuleEvalException extends RuntimeException {
    private String ruleId;

    /**
     * @param ruleId
     * @param message
     * @param e
     */
    public RuleEvalException(String ruleId, String message, Throwable e) {
        super(message, e);
        this.ruleId = ruleId;
    }

    public RuleEvalException(String ruleId, String message) {
        super(message);
        this.ruleId = ruleId;
    }
}
