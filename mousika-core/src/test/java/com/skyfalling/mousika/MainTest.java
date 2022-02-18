package com.skyfalling.mousika;

import com.skyfalling.mousika.engine.RuleEngine;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.skyfalling.mousika.eval.ActionBuilder.build;


public class MainTest {

    @SneakyThrows
    public static void main(String[] args) {

        System.out.println(build("c1?101?true:false:null"));
        System.out.println(build("c1?!101&&!102?true:false:null"));
        System.out.println(build("1?2:3?4?5:6:7"));
        System.out.println(build("1&&2"));
    }


    @AllArgsConstructor
    static class ExprPair {
        String expr1, exp2;
    }


    @SneakyThrows
    @Test
    public void ruleRuleEngine() {
        RuleEngine ruleEngine = new RuleEngine();
        Object res = ruleEngine.evalExpr("[5,6].indexOf($.q)!=-1", new HashMap<String, Integer>() {
            {
                put("q", 5);
            }
        }, null);
        System.out.println(res);
        assert (Boolean) res;
        Object res2 = ruleEngine.evalExpr("$.q != true", new HashMap<String, Boolean>() {
            {
                put("q", true);
            }
        }, null);
        System.out.println(res2);
        assert !(Boolean) res2;
    }


    @Test
    public void testDesc() {
        RuleEngine ruleEngine = new RuleEngine();
        String desc = "代理商【{$.agentId}】不允许【{$.customerId}】跨开{}";
        desc = "\"" + desc.replaceAll("\\{(\\$+\\..+?)\\}", "\\\"+$1+\\\"") + "\"";
        System.out.println(desc);
        Map<String, String> map = new HashMap<>();
        map.put("agentId", "a");
        map.put("customerId", "b");
        System.out.println(ruleEngine.evalExpr(desc, map, null));
    }

}
