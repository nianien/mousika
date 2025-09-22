package com.skyfalling.mousika.suite;

import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.UdfDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 规则加载接口定义
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
public interface RuleLoader extends SceneLoader {
    /**
     * 加载规则列表
     */
    List<RuleDefinition> loadRules();

    /**
     * 加载默认UDF定义
     */
    List<UdfDefinition> loadUdfs();

    @Override
    default List<SceneDefinition> loadScenes() {
        return Collections.EMPTY_LIST;
    }


    /**
     * 加载规则套件
     *
     * @return
     */

    default RuleSuite loadSuite() {
        List<RuleDefinition> ruleDefinitions = new ArrayList<>(this.loadRules());
        List<UdfDefinition> udfDefinitions = new ArrayList<>(this.loadUdfs());
        return new RuleSuite(ruleDefinitions, udfDefinitions, loadScenes());
    }


}
