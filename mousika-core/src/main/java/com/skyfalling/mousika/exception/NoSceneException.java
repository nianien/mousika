package com.skyfalling.mousika.exception;

import lombok.Getter;

/**
 * 没有匹配的规则场景
 *
 * @author liyifei
 * Created on 2021-11-19
 */
@Getter
public class NoSceneException extends RuntimeException {
    private String sceneId;

    /**
     *
     */
    public NoSceneException(String sceneId, String message, Throwable e) {
        super(message, e);
        this.sceneId = sceneId;
    }

    public NoSceneException(String sceneId, String message) {
        super(message);
        this.sceneId = sceneId;
    }
}