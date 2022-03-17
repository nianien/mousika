package com.skyfalling.mousika;

import com.skyfalling.mousika.engine.RuleEngine;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.skyfalling.mousika.eval.ActionBuilder.build;
import static org.junit.Assert.assertEquals;


public class NodeParseTest {

    @Test
    public void testParse() {
        String expr = "!!((a&&b)||(c&&d))";
        System.out.println(expr = build(expr).expr());
        assertEquals("(a&&b)||(c&&d)", expr);
        System.out.println(expr = build("c1?101?true:false:null").expr());
        assertEquals("c1 ? 101 ? true : false : null", expr);
        System.out.println(expr = build("c1?!101&&!102?true:false:null").expr());
        assertEquals("c1 ? !101&&!102 ? true : false : null", expr);
        System.out.println(expr = build("1?((2||3)&&(4||5)?6:7):(8&&9||10&&11?12:13)").expr());
        assertEquals("1 ? (2||3)&&(4||5) ? 6 : 7 : (8&&9)||(10&&11) ? 12 : 13", expr);
        System.out.println(expr = build("(!(!(1&&2)||(4&&5))&&((6||7)))").expr());
        assertEquals("!(!(1&&2)||(4&&5))&&(6||7)", expr);

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
        RuleEngine ruleEngine = new RuleEngine();
        String desc = "代理商【{$.agentId}】不允许【{$.customerId}】跨开{}";
        desc = "\"" + desc.replaceAll("\\{(\\$+\\..+?)\\}", "\\\"+$1+\\\"") + "\"";
        System.out.println(desc);
        Map<String, String> map = new HashMap<>();
        map.put("agentId", "a");
        map.put("customerId", "b");
        System.out.println(ruleEngine.evalExpr(desc, map, null));
        assertEquals("代理商【a】不允许【b】跨开{}", ruleEngine.evalExpr(desc, map, null));
    }

}
