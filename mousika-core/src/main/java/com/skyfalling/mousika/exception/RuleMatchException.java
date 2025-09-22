package com.skyfalling.mousika.exception;

import lombok.Getter;

/**
 * 没有匹配的规则
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 * Created on 2021-11-19
 */
@Getter
public class RuleMatchException extends RuntimeException {
    private String sceneId;

    /**
     *
     */
    public RuleMatchException(String sceneId, String message, Throwable e) {
        super(message, e);
        this.sceneId = sceneId;
    }

    public RuleMatchException(String sceneId, String message) {
        super(message);
        this.sceneId = sceneId;
    }
}