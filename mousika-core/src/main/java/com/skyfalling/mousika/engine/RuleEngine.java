package com.skyfalling.mousika.engine;

import com.skyfalling.mousika.eval.result.NaResult;
import com.skyfalling.mousika.utils.Constants;
import lombok.Builder;
import lombok.Singular;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.script.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");

    {
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("polyglot.js.nashorn-compat", true);
    }

    /**
     * 规则定义
     */
    private Map<String, RuleDefinition> ruleDefinitions = new HashMap<>();
    /**
     * 编译规则, key=expression, value=CompiledScript
     */
    private Map<String, CompiledScript> compiledScripts = new HashMap<>();
    /**
     * 编译描述, key=desc, value=CompiledScript
     */
    private Map<String, CompiledScript> compiledDesc = new HashMap<>();
    /**
     * 编译后的UDF
     */
    private Map<String, Object> compiledUdfs;

    @Builder
    public RuleEngine(@Singular List<RuleDefinition> ruleDefinitions, @Singular List<UdfDefinition> udfDefinitions) {
        //添加默认规则定义
        this.register(new RuleDefinition(Constants.TRUE, Constants.TRUE, "SUCCESS"));
        this.register(new RuleDefinition(Constants.FALSE, Constants.FALSE, "FAILED"));
        this.register(new RuleDefinition(Constants.NULL, "Java.type('" + NaResult.class.getName() + "').DEFAULT", "NULL"));
        this.register(new RuleDefinition(Constants.NOP, "Java.type('" + NaResult.class.getName() + "').DEFAULT", "NOP"));
        ruleDefinitions.forEach(this::register);
        this.compiledUdfs = new UdfContainer(udfDefinitions, (name, func) -> {
            //编译JS函数
            try {
                engine.eval(func);
                return engine.getBindings(ScriptContext.ENGINE_SCOPE).get(name);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        }).compile();
    }

    /**
     * 执行规则
     *
     * @param ruleId 规则名
     */
    public Object evalRule(String ruleId, Object root, Object context) {
        RuleDefinition ruleDefinition = this.ruleDefinitions.get(ruleId);
        if (ruleDefinition == null) {
            throw new IllegalArgumentException("unregistered rule:" + ruleId);
        }
        return doEval(compile(ruleDefinition.getExpression()), root, context);
    }

    /**
     * 解析规则描述
     */
    public String evalRuleDesc(String ruleId, Object root, Object context) {
        RuleDefinition ruleDefinition = this.ruleDefinitions.get(ruleId);
        if (ruleDefinition == null) {
            throw new IllegalArgumentException("unregistered rule:" + ruleId);
        }
        return (String) doEval(compileDesc(ruleDefinition.getDesc()), root, context);
    }

    /**
     * 执行表达式
     */
    public Object evalExpr(String expression, Object root, Object context) {
        return doEval(compile(expression), root, context);
    }


    /**
     * 执行编译脚本
     */
    @SneakyThrows
    private Object doEval(CompiledScript script, Object root, Object context) {
        Bindings bindings = engine.createBindings();
        bindings.put("$", root);
        bindings.put("$$", context);
        bindings.putAll(compiledUdfs);
        return script.eval(bindings);
    }

    /**
     * 注册规则
     */
    private void register(RuleDefinition definition) {
        if (ruleDefinitions.containsKey(definition.getRuleId())) {
            throw new IllegalArgumentException("duplicate function defined: " + definition.getRuleId());
        }
        ruleDefinitions.put(definition.getRuleId(), definition);
        if (definition.getUseType() != 2) {
            //符合规则不编译
            compile(definition.getExpression());
        }
        compileDesc(definition.getDesc());
    }

    private CompiledScript compile(String expression) {
        CompiledScript compiledScript = compiledScripts.get(expression);
        if (compiledScript == null) {
            compiledScript = doCompile(expression);
        }
        return compiledScript;
    }


    /**
     * 编译规则描述，形如:{$.agentId}格式的表达式支持参数代入<p/>
     *
     * @param originDesc
     * @return
     */
    private CompiledScript compileDesc(String originDesc) {
        // 正则表达式转换成字符串计算
        // "代理商：{$.agentId} 不允许 客户：{$.customerId}】跨开，这里{不需要转义}"
        // ==> "代理商："+$.agentId+" 不允许 "+$.customerId+"：跨开，这里{不需要转义}"
        String expression = "\"" + originDesc.replaceAll("\\{(\\$+\\..+?)\\}", "\\\"+$1+\\\"") + "\"";
        CompiledScript compiledScript = compiledDesc.get(expression);
        if (compiledScript == null) {
            compiledScript = doCompile(expression);
        }
        return compiledScript;
    }


    /**
     * 编译JS脚本
     *
     * @param expression
     * @return
     */
    @SneakyThrows
    private synchronized CompiledScript doCompile(String expression) {
        return ((Compilable) engine).compile(expression);
    }


}