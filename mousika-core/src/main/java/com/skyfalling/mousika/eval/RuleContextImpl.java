package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.listener.ListenerProvider;
import com.skyfalling.mousika.eval.listener.RuleEvent;
import com.skyfalling.mousika.eval.listener.RuleEvent.EventType;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.exception.RuleEvalException;
import com.skyfalling.mousika.expr.DefaultNodeVisitor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 规则上下文默认实现
 *
 * @author liyifei
 */
@Slf4j
@Getter
public class RuleContextImpl extends LinkedHashMap<String, Object> implements RuleContext {

    /**
     * 执行引擎
     */
    private RuleEngine ruleEngine;
    /**
     * 待匹配对象
     */
    private Object data;
    /**
     * 缓存评估结果
     */
    private Map<String, EvalResult> evalCache = new LinkedHashMap<>();
    /**
     * 上下文自定义属性
     */
    private Map<String, Object> properties = new HashMap<>();

    /**
     * 用于规则分析
     */
    private DefaultNodeVisitor visitor = new DefaultNodeVisitor(this);

    /**
     * 指定执行引擎和规则对象
     *
     * @param ruleEngine
     * @param data
     */
    public RuleContextImpl(RuleEngine ruleEngine, Object data) {
        this.ruleEngine = ruleEngine;
        this.data = data;
    }


    /**
     * 评估单个规则
     *
     * @param ruleId 规则ID
     */
    @Override
    public EvalResult eval(String ruleId) {
        return evalCache.computeIfAbsent(ruleId, this::doEval);
    }


    /**
     * 执行规则评估
     *
     * @param ruleId 规则ID
     */
    private EvalResult doEval(String ruleId) {
        try {
            EvalResult result = new EvalResult(ruleEngine.evalRule(ruleId, data, this));
            ListenerProvider.DEFAULT.onEval(new RuleEvent(EventType.EVAL_SUCCEED, ruleId, result));
            return result;
        } catch (Exception e) {
            ListenerProvider.DEFAULT.onEval(new RuleEvent(EventType.EVAL_FAIL, ruleId, e));
            throw new RuleEvalException(ruleId, e.getMessage(), e);
        }

    }


    @Override
    public List<RuleResult> getEvalResults() {
        List<RuleResult> results = new ArrayList<>();
        for (String rule : visitor.getEffectiveRules()) {
            String ruleDesc = ruleEngine.evalRuleDesc(rule, data, this);
            results.add(new RuleResult(rule, evalCache.get(rule), ruleDesc));
        }
        return results;
    }

    @Override
    public Object getProperty(Object name) {
        return super.get(name);
    }

    @Override
    public void setProperty(String name, Object value) {
        super.put(name, value);
    }


    @Override
    public EvalResult visit(RuleNode node) {
        return visitor.visit(node);
    }

    @Override
    public void reset(OpFlag flag) {
        visitor.reset(flag);
    }
}








