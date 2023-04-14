package com.skyfalling.mousika.udf;


import com.skyfalling.mousika.annotation.Udf;
import com.skyfalling.mousika.eval.context.RuleContext;
import com.skyfalling.mousika.eval.context.UdfContext;
import com.skyfalling.mousika.udf.Functions.Function4;

import java.util.Map;

/**
 * @author lichaoshuai
 * Created on 2022-08-26
 */
@Udf(group = "sys")
public class CheckSceneUdf implements Function4<String, Object, UdfContext, Map<String, Object>, Object> {

    public Object apply(String sceneId, Object param, UdfContext ruleContext, Map<String, Object> args) {
        System.out.println("CheckSceneUdf execute");
        return "success";
    }

}
