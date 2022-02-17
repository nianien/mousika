package com.skyfalling.mousika.engine;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.script.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        this.register(new RuleDefinition("true", "true", "通过"));
        this.register(new RuleDefinition("false", "false", "拒绝"));
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
        sourceScripts.put(id, expression);
        compiledScripts.put(expression, ((Compilable) engine).compile(expression));
        descriptions.put(id, desc);
    }


    /**
     * 注册自定义函数
     */
    public void register(UdfDefinition udfDefinition) {
        register(udfDefinition.getName(), udfDefinition.getUdf());
    }

    /**
     * 注册自定义函数
     */
    protected void register(String name, Object function) {
        if (udfs.containsKey(name)) { //先普通udf，再注册service时，存在覆盖情况
            log.info("udf function:{} is replace!", name);
        }
        udfs.put(name, function);
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
        // "代理商【{$.agentId}】不允许【{$.customerId}】跨开{不需要转义}" ==> "代理商【"+$.agentId+"】不允许【"+$.customerId+"】跨开{不需要转义}"
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
