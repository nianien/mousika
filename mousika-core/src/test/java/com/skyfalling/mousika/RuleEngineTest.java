package com.skyfalling.mousika;

import com.skyfalling.mousika.engine.RuleEngine;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.script.*;
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
    public void ruleRuleEngine2() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("polyglot.js.nashorn-compat", true);
        engine.eval("function add(a,b){return a+b};");
        Object o1 = engine.getBindings(ScriptContext.ENGINE_SCOPE).get("add");
        engine.eval("function add(a,b){return a-b};");
        Object o2 = engine.getBindings(ScriptContext.ENGINE_SCOPE).get("add");
        Bindings bindings1 = engine.createBindings();
        bindings1.put("add1", o1);
        bindings1.put("add2", o2);
        CompiledScript compile1 = ((Compilable) engine).compile("add1(1,1)");
        System.out.println(compile1.eval(bindings1));
        CompiledScript compile2 = ((Compilable) engine).compile("add2(1,1)");
        System.out.println(compile2.eval(bindings1));
    }

    @SneakyThrows
    @Test
    public void testRuleEngine() {


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
