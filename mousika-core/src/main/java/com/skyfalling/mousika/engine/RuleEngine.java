package com.skyfalling.mousika.engine;

import com.cudrania.core.utils.StringUtils;
import com.skyfalling.mousika.eval.result.NaResult;
import com.skyfalling.mousika.udf.Functions;
import com.skyfalling.mousika.udf.UdfDelegate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.script.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 规则引擎
 *
 * @author liyifei
 */
@Slf4j
public class RuleEngine {

    /**
     * 脚本引擎
     */
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    /**
     * 源脚本
     */
    private Map<String, String> sourceScripts = new ConcurrentHashMap<>();
    /**
     * 规则对应的描述
     */
    private Map<String, String> descriptions = new ConcurrentHashMap<>();
    /**
     * 编译脚本
     */
    private Map<String, CompiledScript> compiledScripts = new ConcurrentHashMap<>();
    /**
     * UDF列表
     */
    private Map<String, Object> udfs = new ConcurrentHashMap<>();


    {
        //添加默认规则定义
        this.register(new RuleDefinition("true", "true", "SUCCESS"));
        this.register(new RuleDefinition("false", "false", "FAILED"));
        this.register(new RuleDefinition("null", "Java.type('"+ NaResult.class.getName()+"').DEFAULT", "NULL"));
        this.register(new RuleDefinition("nop", "Java.type('"+ NaResult.class.getName()+"').DEFAULT", "NOP"));
    }

    /**
     * 注册规则
     */
    public void register(RuleDefinition definition) {
        this.register(definition.getRuleId(), definition.getExpression(), definition.getDesc());
    }

    /**
     * 注册规则
     *
     * @param id         规则id
     * @param expression 规则表达式
     * @param desc       规则描述
     */
    @SneakyThrows
    protected void register(String id, String expression, String desc) {
        if (sourceScripts.containsKey(id)) {
            throw new IllegalArgumentException("function: " + id + " is already defined!");
        }
        descriptions.put(id, desc);
        sourceScripts.put(id, expression);
    }


    /**
     * 注册自定义函数
     *
     * @param udfDefinition
     */
    public void register(UdfDefinition udfDefinition) {
        register(udfDefinition.getGroup(), udfDefinition.getName(), udfDefinition.getUdf());
    }

    /**
     * 注册自定义函数
     *
     * @param group
     * @param name
     * @param udf
     */
    protected void register(String group, String name, Object udf) {
        Map map = this.udfs;
        if (StringUtils.isNotEmpty(group)) {
            String[] tokens = group.replaceAll("\\s", "").split("\\.+");
            int i = 0;
            for (; i < tokens.length; i++) {
                String token = tokens[i];
                Object o = map.computeIfAbsent(token, k -> new HashMap<>());
                if (o instanceof Map) {
                    map = (Map) o;
                } else {
                    String conflictName = Arrays.stream(tokens).limit(i + 1).collect(Collectors.joining("."));
                    throw new IllegalArgumentException("udf: " + conflictName + " is already defined!");
                }
            }
        }
        if (map.containsKey(name)) {
            throw new IllegalArgumentException("udf: " + name + " is already defined!");
        }
        doRegister(map, name, udf);
    }

    /**
     * 注册自定义函数
     *
     * @param map
     * @param name
     * @param udf
     */
    protected void doRegister(Map map, String name, Object udf) {
        Class<?>[] interfaces = udf.getClass().getInterfaces();
        for (Class<?> anInterface : interfaces) {
            //只代理通过Functions定义的udf
            if (anInterface.getName().indexOf(Functions.class.getName()) != -1) {
                map.put(name, UdfDelegate.of(udf));
                return;
            }
        }
        map.put(name, udf);
    }

    /**
     * 执行规则
     *
     * @param ruleId 规则名
     */
    @SneakyThrows
    public Object evalRule(String ruleId, Object root, Object context) {
        String sourceScript = sourceScripts.get(ruleId);
        if (sourceScript == null) {
            throw new IllegalArgumentException("unregistered rule:" + ruleId);
        }
        CompiledScript compiledScript = this.compile(sourceScript, true);
        return doEval(compiledScript, root, context);
    }

    /**
     * 解析规则描述
     */
    public String evalRuleDesc(String ruleId, Object root, Object context) {
        String originDesc = this.descriptions.get(ruleId);
        if (originDesc == null || originDesc.isEmpty()) {
            return "";
        }
        // 正则表达式转换成字符串计算
        // "代理商【{$.agentId}】不允许【{$.customerId}】跨开{不需要转义}"
        // ==> "代理商【"+$.agentId+"】不允许【"+$.customerId+"】跨开{不需要转义}"
        originDesc = "\"" + originDesc.replaceAll("\\{(\\$+\\..+?)\\}", "\\\"+$1+\\\"") + "\"";
        return (String) evalExpr(originDesc, root, context);
    }

    /**
     * 执行表达式
     */
    public Object evalExpr(String expression, Object root, Object context) {
        CompiledScript compiledScript = compile(expression, false);
        return doEval(compiledScript, root, context);
    }


    /**
     * 执行注册脚本
     */
    @SneakyThrows
    private Object doEval(CompiledScript script, Object root, Object context) {
        Bindings bindings = engine.createBindings();
        bindings.putAll(udfs);
        bindings.put("$", root);
        bindings.put("$$", context);
        return script.eval(bindings);
    }

    @SneakyThrows
    private CompiledScript compile(String expression, boolean cache) {
        CompiledScript compiledScript = compiledScripts.get(expression);
        if (compiledScript == null) {
            compiledScript = ((Compilable) engine).compile(expression);
            if (cache) {
                compiledScripts.put(expression, compiledScript);
            }
        }
        return compiledScript;
    }

}