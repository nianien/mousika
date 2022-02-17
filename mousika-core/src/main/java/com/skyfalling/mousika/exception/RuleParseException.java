package com.skyfalling.mousika.exception;

import lombok.Data;

/**
 * 规则解析异常
 *
 * @author liyifei
 * Created on 2021-11-19
 */
@Data
public class RuleParseException extends RuntimeException {
    private String expr;

    /**
     * @param expr
     * @param message
     * @param e
     */
    public RuleParseException(String expr, String message, Throwable e) {
        super(message, e);
        this.expr = expr;
    }

    public RuleParseException(String expr, String message) {
        super(message);
        this.expr = expr;
    }
}
