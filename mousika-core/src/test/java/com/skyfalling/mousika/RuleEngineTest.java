package com.skyfalling.mousika;

import com.skyfalling.mousika.engine.RuleEngine;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created on 2023/3/31
 *
 * @author liyifei
 */
public class RuleEngineTest {
    private RuleEngine ruleEngine = RuleEngine.builder().build();

    @SneakyThrows
    @Test
    public void ruleRuleEngine() {
        Object res = ruleEngine.evalExpr("[5,6].indexOf($.q)!=-1", new HashMap<String, Integer>() {
            {
                put("q", 5);
            }
        }, null);
        System.out.println(res);
        assertEquals(res, true);
        Object res2 = ruleEngine.evalExpr("$.q != true", new HashMap<String, Boolean>() {
            {
                put("q", true);
            }
        }, null);
        System.out.println(res2);
        assertEquals(res2, false);
    }


    @Test
    public void testDesc() {
        String desc = "代理商【{$.agentId}】不允许【{$.customerId}】跨开{}";
        desc = "\"" + desc.replaceAll("\\{(\\$+\\..+?)\\}", "\\\"+$1+\\\"") + "\"";
        System.out.println(desc);
        Map<String, String> map = new HashMap<>();
        map.put("agentId", "a");
        map.put("customerId", "b");
        System.out.println(ruleEngine.evalExpr(desc, map, null));
        assertEquals("代理商【a】不允许【b】跨开{}", ruleEngine.evalExpr(desc, map, null));
    }

    //number类型计算
    @Test
    public void testNumberTypeEval() {
        Object res = ruleEngine.evalExpr("$.liveZuanCurrentMonth*1", new HashMap<String, Object>() {
            {
                put("liveZuanCurrentMonth", 5);
            }
        }, null);
        System.out.println(res);
        assertEquals(res, 5.0);

        Object res2 = ruleEngine.evalExpr("5*1", new HashMap<String, Integer>() {
        }, null);
        System.out.println(res2);
        assertEquals(res2, 5);
    }
}
