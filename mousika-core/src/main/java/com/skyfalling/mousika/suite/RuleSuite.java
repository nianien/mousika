package com.skyfalling.mousika.suite;

import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.RuleEvaluator;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.result.NodeResult;
import com.skyfalling.mousika.exception.NoSceneException;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 规则套件,针对每个规则场景应用所关联规则集合
 *
 * @author liyifei <liyifei@kuaishou.com>
 */
@Getter
public class RuleSuite {
    /**
     * 规则判定器
     */
    private final RuleEvaluator ruleEvaluator;
    /**
     * 场景列表
     */
    private final Map<String, RuleScene> scenes;


    private static RuleSuite current;

    {
        current = this;
    }

    /**
     * 获取当前规则套件
     *
     * @return
     */
    public static RuleSuite get() {
        return current;
    }

    /**
     * @param ruleEvaluator    规则评估器
     * @param sceneDefinitions 场景定义
     */
    public RuleSuite(RuleEvaluator ruleEvaluator, List<SceneDefinition> sceneDefinitions) {
        this.ruleEvaluator = ruleEvaluator;
        this.scenes = sceneDefinitions.stream().map(SceneDefinition::build)
                .collect(Collectors.toMap(RuleScene::getId, Function.identity(), (v1, v2) -> v2));
    }

    /**
     * 获取规则场景
     *
     * @param sceneKey
     * @return
     */
    public RuleScene getRuleScene(String sceneKey) {
        return scenes.get(sceneKey);
    }

    /**
     * 校验规则集合
     *
     * @param ruleNode
     * @param target
     * @param <T>
     * @return
     */
    public <T> NodeResult check(RuleNode ruleNode, T target) {
        return ruleEvaluator.eval(ruleNode, target);
    }


    /**
     * 校验场景
     *
     * @param sceneId     场景ID
     * @param ruleContext 规则上下文
     * @return
     */
    public NodeResult check(String sceneId, RuleContext ruleContext) {
        RuleScene ruleScene = scenes.get(String.valueOf(sceneId));
        if (ruleScene == null) {
            throw new NoSceneException(sceneId, "no scene defined:" + sceneId);
        }
        return ruleEvaluator.doEval(ruleScene.getRuleNode(), ruleContext, false);
    }


    /**
     * 评估表达式
     *
     * @param expr   规则表达式
     * @param target
     * @return
     */
    public NodeResult check(String expr, Object target) {
        return this.ruleEvaluator.eval(expr, target);
    }
}
