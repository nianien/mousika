package com.skyfalling.mousika.suite;


import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.RuleChecker;
import com.skyfalling.mousika.suite.RuleScenario;
import com.skyfalling.mousika.suite.RuleSuite;

import java.util.List;
import java.util.function.Supplier;

/**
 * 规则加载接口定义
 *
 * @author liyifei 
 */
public interface RuleLoader extends Supplier<RuleSuite> {


    /**
     * 加载规则列表
     *
     * @return
     */
    List<RuleDefinition> loadRules();

    /**
     * 加载规则场景
     *
     * @return
     */
    List<RuleScenario> loadScenarios();


    /**
     * 加载UDF定义
     *
     * @return
     */
    List<UdfDefinition> loadUdfs();


    /**
     * 加载规则套件
     *
     * @return
     */
    @Override
    default RuleSuite get() {
        List<RuleDefinition> ruleDefinitions = this.loadRules();
        List<UdfDefinition> udfDefinitions = this.loadUdfs();
        List<RuleScenario> scenarioList = this.loadScenarios();
        RuleEngine ruleEngine = new RuleEngine();
        for (RuleDefinition ruleDefinition : ruleDefinitions) {
            ruleEngine.register(ruleDefinition);
        }
        for (UdfDefinition udfDefinition : udfDefinitions) {
            ruleEngine.register(udfDefinition);
        }
        return new RuleSuite(new RuleChecker(ruleEngine), scenarioList);
    }

}
