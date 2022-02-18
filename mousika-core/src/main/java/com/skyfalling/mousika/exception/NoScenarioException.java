package com.skyfalling.mousika.exception;

import lombok.Data;

/**
 * 没有匹配的规则场景
 *
 * @author liyifei
 * Created on 2021-11-19
 */
@Data
public class NoScenarioException extends RuntimeException {
    private String scenarioId;

    /**
     * @param scenarioId
     * @param message
     * @param e
     */
    public NoScenarioException(String scenarioId, String message, Throwable e) {
        super(message, e);
        this.scenarioId = scenarioId;
    }

    public NoScenarioException(String scenarioId, String message) {
        super(message);
        this.scenarioId = scenarioId;
    }
}
