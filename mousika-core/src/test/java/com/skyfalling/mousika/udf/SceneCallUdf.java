package com.skyfalling.mousika.udf;


import com.skyfalling.mousika.annotation.Udf;
import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.result.NodeResult;
import com.skyfalling.mousika.suite.RuleSuite;
import com.skyfalling.mousika.udf.Functions.Function2;

/**
 * @author lichaoshuai
 * Created on 2022-08-16
 */
@Udf
public class SceneCallUdf implements Function2<String, RuleContext, Object> {

    @Override
    public Object apply(String sceneId, RuleContext ruleContext) {
        RuleSuite ruleSuite = RuleSuite.get();
        RuleContext copiedContext = ruleContext.copy(null);
        NodeResult result = ruleSuite.check(sceneId, copiedContext);
        return result.getResult();
    }
}
