package com.skyfalling.mousika.suite;

import java.util.List;

/**
 * 场景加载接口定义
 *
 * @author liujiakun03 <liujiakun03@kuaishou.com>
 * Created on 2022-08-03
 */
public interface SceneLoader {
    /**
     * 加载规则场景
     */
    List<SceneDefinition> loadScenes();
}
