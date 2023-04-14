package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.annotation.Udf;
import com.skyfalling.mousika.eval.result.NodeResult;
import com.skyfalling.mousika.suite.RuleSuite;
import com.skyfalling.mousika.udf.Functions.Function2;
import com.skyfalling.mousika.udf.Functions.Function3;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 场景评估函数，使用方式如下：<br/>
 * <p>
 * <code>sys.scene.eval(<场景ID>,<执行参数>,[附加上下文])</code>
 * </p>
 * 如果要使用当前规则的参数和上下文，可如下调用:<br/>
 * <p>
 * <code>sys.scene.eval(<场景ID>,$,$$)</code>
 * </p>
 *
 * @author liyifei
 * Created on 2022-08-26
 */
@Udf(group = "sys.scene", value = "eval")
@Slf4j
public class EvalSceneUdf implements Function2<String, Object, Object>, Function3<String, Object, Map<String, Object>, Object> {


    /**
     * 调用场景, 传入指定参数和上下文
     *
     * @param sceneId 场景id
     * @param target  执行参数
     * @param context 执行上下文
     * @return 场景调用结果
     */
    public Object apply(String sceneId, Object target, Map<String, Object> context) {
        RuleSuite ruleSuite = RuleSuite.get();
        NodeResult result = ruleSuite.evalScene(sceneId, target, context);
        return result.getResult();
    }

    /**
     * 调用场景, 传入指定参数
     *
     * @param sceneId 场景id
     * @param target  执行参数
     * @return 场景调用结果
     */
    public Object apply(String sceneId, Object target) {
        RuleSuite ruleSuite = RuleSuite.get();
        NodeResult result = ruleSuite.evalScene(sceneId, target);
        return result.getResult();
    }


}
