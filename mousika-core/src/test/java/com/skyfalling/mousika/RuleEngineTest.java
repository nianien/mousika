package com.skyfalling.mousika;

import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.script.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created on 2023/3/31
 *
 * @author liyifei
 */
public class RuleEngineTest {

    @SneakyThrows
    @Test
    public void ruleRuleEngine2() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("polyglot.js.nashorn-compat", true);
        CompiledScript compiledScript = ((Compilable) engine).compile("add1(1,1)");
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    ScriptEngine se = new ScriptEngineManager().getEngineByName("graal.js");
                    se.eval("function add(a,b){return a+b};");
                    Bindings bindings1 = engine.createBindings();
                    bindings1.put("add1", se.getBindings(ScriptContext.ENGINE_SCOPE).get("add"));
                    System.out.println(compiledScript.eval(bindings1));
                } catch (ScriptException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }


    /**
     * 测试js函数
     */
    @SneakyThrows
    @Test
    public void testJsUdf0() {
        RuleEngine.RuleEngineBuilder builder = RuleEngine.builder();
        builder.udfDefinition(new UdfDefinition("jdUdf", "test", """
                function test(begin,end) {
                    var sum = begin; for (var i = 0; i < end; i++) {
                        sum = sum + i % 8
                    }
                    return sum;
                }
                """));
        RuleEngine engine = builder.build();
        Object object = engine.evalExpr("jdUdf.test(1001,10000)", null, null);
        System.out.println(object);
    }

    /**
     * 测试多线程下的js函数
     */
    @SneakyThrows
    @Test
    public void testJsUdf() {
        RuleEngine.RuleEngineBuilder builder = RuleEngine.builder();
        builder.udfDefinition(new UdfDefinition("jdUdf", "test", """
                function test(begin) {
                    var sum = begin; for (var i = 0; i < 1000000; i++) {
                        sum = sum + i % 8
                    }
                    return sum;
                }
                """));
        RuleEngine engine = builder.build();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int begin = i;
            Thread thread = new Thread(() -> {
                Object object = engine.evalExpr("jdUdf.test(" + begin + ")", null, null);
                System.out.println(object);
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }


    @Test
    public void testDesc() {
        RuleEngine ruleEngine = RuleEngine.builder().build();
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
        RuleEngine ruleEngine = RuleEngine.builder().build();
        Object res = ruleEngine.evalExpr("$.currentMonth*1.0", new HashMap<String, Object>() {
            {
                put("currentMonth", 5);
            }
        }, null);
        System.out.println(res);
        assertEquals(5,res);

        Object res2 = ruleEngine.evalExpr("""
                ({"result":true, "count":42});
                """, new HashMap<String, Integer>() {
        }, null);
        System.out.println(res2.getClass());
    }
}
