package com.skyfalling.mousika;

import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.node.ActionNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static com.skyfalling.mousika.eval.ActionBuilder.build;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class NodeParseTest {

    @ParameterizedTest
    @CsvSource(
            {
                    "!!((a&&b&&c)||(c||d||e)),(a&&b&&c)||(c||d||e)",
                    "c1?101?true:false:null,c1 ? 101 ? true : false : null",
                    "c1?!101&&!102?true:false:null,c1 ? (!101&&!102) ? true : false : null",
                    "1?((2||3)&&(4||5)?6:7):(8&&9?10&&11:12),1 ? ((2||3)&&(4||5)) ? 6 : 7 : (8&&9) ? (10&&11) : 12",
                    "(!(!(1&&2)||(4&&5))&&((6||7))),!(!(1&&2)||(4&&5))&&(6||7)",
                    "(((1&&2&&3))||((a||b||c))),(1&&2&&3)||(a||b||c)"
            }
    )
    public void testParse(String expr, String expected) {
        ActionNode node = build(expr);
        System.out.println(expr = node.expr());
        assertEquals(expected, expr);
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
