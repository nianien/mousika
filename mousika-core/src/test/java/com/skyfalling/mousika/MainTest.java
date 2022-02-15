package com.skyfalling.mousika;

import com.google.gson.Gson;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.RuleChecker;
import com.skyfalling.mousika.eval.json.JsonUtils;
import com.skyfalling.mousika.eval.node.RuleNode;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MainTest {

    @SneakyThrows
    public static void main(String[] args) {
        RuleNode ruleNode = RuleChecker.parse("!101&&!102");
        System.out.println(ruleNode);
        System.out.println(new Gson().toJson(ruleNode));
        System.out.println(JsonUtils.toJson(ruleNode));


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
