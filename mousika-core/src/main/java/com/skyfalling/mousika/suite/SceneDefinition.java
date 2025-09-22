package com.skyfalling.mousika.suite;

import com.cudrania.core.utils.StringUtils;
import com.skyfalling.mousika.eval.parser.NodeBuilder;
import com.skyfalling.mousika.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 场景定义
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
@Data
@AllArgsConstructor
public class SceneDefinition {

    /**
     * 场景ID
     */
    private String id;
    /**
     * 场景配置信息
     */
    private String config;

    /**
     * 规则集配置
     */
    private String ruleSetConfig;



    public RuleScene build() {
        SceneConfig sceneConfig =
                StringUtils.isNotBlank(config) ? JsonUtils.toBean(config, SceneConfig.class) : new SceneConfig();
        return new RuleScene(id, sceneConfig, NodeBuilder.build(ruleSetConfig));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SceneConfig {
        /**
         * 解析类型
         */
        private int resultParseType;
    }

}
