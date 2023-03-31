package com.skyfalling.mousika.suite;

import java.util.*;
import com.skyfalling.mousika.engine.*;
import com.skyfalling.mousika.eval.RuleEvaluator;
import com.skyfalling.mousika.eval.parser.NodeBuilder;
import com.skyfalling.mousika.eval.parser.NodeGenerator;

/**
 * 规则加载接口定义
 *
 * @author liyifei
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
        return new RuleSuite(create(ruleDefinitions, udfDefinitions), loadScenes());
    }


    /**
     * 创建规则套件
     *
     * @param ruleDefinitions
     * @param udfDefinitions
     * @return
     */
    default RuleEvaluator create(List<RuleDefinition> ruleDefinitions, List<UdfDefinition> udfDefinitions) {
        Map<String, String> compositeRules = new HashMap<>();
        RuleEngine ruleEngine = new RuleEngine();
        Iterator<RuleDefinition> it = ruleDefinitions.iterator();
        while (it.hasNext()) {
            RuleDefinition ruleDefinition = it.next();
            switch (ruleDefinition.getUseType()) {
                case 1: //决策表
                    String udf = "udf_rule_table_$" + ruleDefinition.getRuleId();
                    //动态注册UDF
                    udfDefinitions
                            .add(new UdfDefinition(udf, RuleTableUdf.fromJson(ruleDefinition.getExpression())));
                    //修改规则表达式
                    ruleDefinition.setExpression(udf + "($)");
                    break;

                case 2: //复合规则
                    compositeRules.put(ruleDefinition.getRuleId(), ruleDefinition.getExpression());
                    break;
                default:
            }
            ruleEngine.register(ruleDefinition);
        }
        for (UdfDefinition udfDefinition : udfDefinitions) {
            ruleEngine.register(udfDefinition);
        }
        NodeBuilder.setGenerator(NodeGenerator.create(compositeRules));
        return new RuleEvaluator(ruleEngine);
    }
}
