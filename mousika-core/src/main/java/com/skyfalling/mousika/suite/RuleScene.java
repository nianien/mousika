package com.skyfalling.mousika.suite;

import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.suite.SceneDefinition.SceneConfig;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 规则场景
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
@Data
@AllArgsConstructor
public class RuleScene {

    /**
     * 场景ID
     */
    private String id;

    /**
     * 场景配置信息
     */
    private SceneConfig config;
    /**
     * 规则节点
     */
    private RuleNode ruleNode;


}
