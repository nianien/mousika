package com.skyfalling.mousika.suite;

import com.skyfalling.mousika.eval.ActionResult;
import com.skyfalling.mousika.eval.NaResult;
import com.skyfalling.mousika.eval.RuleChecker;
import com.skyfalling.mousika.exception.NoScenarioException;
import com.skyfalling.mousika.exception.RuleMatchException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created on 2022/2/11
 *
 * @author liyifei
 */
@AllArgsConstructor
public class RuleSuite {

    /**
     * 规则判定器
     */
    private final RuleChecker ruleChecker;
    /**
     * 场景关联的规则集
     */
    private final Map<String, RuleScenario> scenarios;


    public RuleSuite(RuleChecker ruleChecker, List<RuleScenario> scenarioList) {
        this.ruleChecker = ruleChecker;
        this.scenarios = scenarioList.stream()
                .collect(Collectors.toMap(RuleScenario::getId, Function.identity(), (v1, v2) -> v2));
    }

    /**
     * 判定对象是否符合业务场景
     *
     * @param scenarioId 场景名称
     */
    public ActionResult checkScenario(String scenarioId, Object data) {
        RuleScenario scenario = scenarios.get(scenarioId);
        if (scenario == null) {
            throw new NoScenarioException(scenarioId, "no scenario defined:" + scenarioId);
        }
        ActionResult actionResult = this.ruleChecker.check(scenario.getRuleActions(), data);
        if (actionResult instanceof NaResult) {
            //no suitable rule-set
            throw new RuleMatchException(scenarioId, "not suitable rules for scenario:" + scenario.getId());
        }
        return actionResult;
    }
}
