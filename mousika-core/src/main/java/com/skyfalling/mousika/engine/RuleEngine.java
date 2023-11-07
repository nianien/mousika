package com.skyfalling.mousika.engine;

import com.skyfalling.mousika.eval.result.NaResult;
import com.skyfalling.mousika.utils.Constants;
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
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
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
    private UdfContainer udfContainer = new UdfContainer();


    {
        //添加默认规则定义
        this.register(new RuleDefinition(Constants.TRUE, Constants.TRUE, "SUCCESS"));
        this.register(new RuleDefinition(Constants.FALSE, Constants.FALSE, "FAILED"));
        this.register(new RuleDefinition(Constants.NULL, "Java.type('" + NaResult.class.getName() + "').DEFAULT", "NULL"));
        this.register(new RuleDefinition(Constants.NOP, "Java.type('" + NaResult.class.getName() + "').DEFAULT", "NOP"));

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
        this.udfContainer.register(udfDefinition);
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
        bindings.put("$", root);
        bindings.put("$$", context);
        bindings.putAll(udfContainer.compileUdf());
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