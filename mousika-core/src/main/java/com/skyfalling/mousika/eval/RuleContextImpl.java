package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.listener.ListenerProvider;
import com.skyfalling.mousika.eval.listener.RuleEvent;
import com.skyfalling.mousika.eval.listener.RuleEvent.EventType;
import com.skyfalling.mousika.eval.node.ExprNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.result.EvalResult;
import com.skyfalling.mousika.eval.result.RuleResult;
import com.skyfalling.mousika.exception.RuleEvalException;
import com.skyfalling.mousika.expr.DefaultNodeVisitor;
import com.skyfalling.mousika.expr.DefaultNodeVisitor.EvalNode;
import com.skyfalling.mousika.expr.NodeVisitor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

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
     * 当前规则
     */
    private ThreadLocal<String> currentRule = new ThreadLocal<>();
    /**
     * 缓存评估结果
     */
    private Map<String, EvalResult> evalCache = new ConcurrentSkipListMap<>();

    /**
     * 用于规则分析
     */
    private NodeVisitor visitor = new DefaultNodeVisitor(this);


    /**
     * 指定执行引擎和规则对象
     */
    public RuleContextImpl(RuleEngine ruleEngine, Object data) {
        this.ruleEngine = ruleEngine;
        this.data = data;
    }


    @Override
    public String evalDesc(String ruleId) {
        return ruleEngine.evalRuleDesc(ruleId, data, this);
    }

    @Override
    public String getCurrentRule() {
        return currentRule.get();
    }

    /**
     * 评估单条规则
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
        long begin = System.currentTimeMillis();
        try {
            EvalResult result = new EvalResult(ruleId, ruleEngine.evalRule(ruleId, data, this));
            long end = System.currentTimeMillis();
            ListenerProvider.DEFAULT.onEval(new RuleEvent(EventType.EVAL_SUCCEED, ruleId, result, end - begin));
            return result;
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            ListenerProvider.DEFAULT.onEval(
                    new RuleEvent(EventType.EVAL_FAIL, ruleId, e, end - begin));
            throw new RuleEvalException(ruleId, e.getMessage(), e);
        }

    }


    @Override
    public List<RuleResult> collect() {
        List<RuleResult> ruleResults = visitor.getEvalRules()
                .stream()
                .map(this::transform)
                .collect(Collectors.toList());
        visitor.finish();
        return ruleResults;
    }


    /**
     * 转换规则执行结果
     *
     * @param node
     * @return
     */
    private RuleResult transform(EvalNode node) {
        String expr = node.getExpr();
        EvalResult result = evalCache.get(expr);
        RuleResult ruleResult = new RuleResult(result, evalDesc(expr));
        for (EvalNode subNode : node.getChildren()) {
            ruleResult.getSubRules().add(transform(subNode));
        }
        return ruleResult;
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
    public RuleContext copy(Map<String, Object> extra) {
        RuleContextImpl context = new RuleContextImpl(ruleEngine, data);
        context.putAll(this);
        context.evalCache.putAll(this.evalCache);
        if (extra != null && !extra.isEmpty()) {
            context.putAll(extra);
        }
        return context;
    }


    @Override
    public EvalResult visit(RuleNode node) {
        RuleNode origin = node;
        if (origin instanceof ExprNode) {
            this.currentRule.set(node.expr());
        }
        return visitor.visit(node);
    }

    @Override
    public List<EvalNode> getEvalRules() {
        return visitor.getEvalRules();
    }


    @Override
    public void finish() {
        visitor.finish();
    }
}








