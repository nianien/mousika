package com.skyfalling.mousika.exception;

/**
 * 规则场景异常
 *
 * @author liyifei
 * Created on 2021-11-19
 */
public class RuleScenarioException extends RuntimeException {
    private String scenarioId;

    /**
     * @param scenarioId
     * @param message
     * @param e
     */
    public RuleScenarioException(String scenarioId, String message, Throwable e) {
        super(message, e);
        this.scenarioId = scenarioId;
    }

    public RuleScenarioException(String scenarioId, String message) {
        super(message);
        this.scenarioId = scenarioId;
    }
}
