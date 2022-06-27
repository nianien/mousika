package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.listener.ListenerProvider;
import com.skyfalling.mousika.eval.listener.RuleEvent;
import com.skyfalling.mousika.eval.listener.RuleEvent.EventType;
import com.skyfalling.mousika.eval.node.ActionNode;
import com.skyfalling.mousika.eval.node.ExprNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.exception.RuleEvalException;
import com.skyfalling.mousika.expr.DefaultNodeVisitor;
import com.skyfalling.mousika.expr.NodeVisitor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
    private String ruleId;
    /**
     * 缓存评估结果
     */
    private Map<String, EvalResult> evalCache = new LinkedHashMap<>();
    /**
     * 用于规则分析
     */
    private NodeVisitor visitor = new DefaultNodeVisitor(this);


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


    @Override
    public String evalDesc(String ruleId) {
        return ruleEngine.evalRuleDesc(ruleId, data, this);
    }

    @Override
    public String getCurrentRule() {
        return ruleId;
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
    public List<List<RuleResult>> getEvalResults() {
        List<List<RuleResult>> groups = new ArrayList<>();
        if (visitor instanceof DefaultNodeVisitor) {
            Map<String, List<String>> effectiveRules = ((DefaultNodeVisitor) visitor).getEffectiveRules();
            for (Entry<String, List<String>> rules : effectiveRules.entrySet()) {
                List<RuleResult> res = new ArrayList<>();
                for (String rule : rules.getValue()) {
                    res.add(new RuleResult(rules.getKey(), rule, evalCache.get(rule), evalDesc(rule)));
                }
                groups.add(res);
            }
        }
        return groups;
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
        RuleNode origin = node;
        if (node instanceof NodeWrapper) {
            origin = ((NodeWrapper) node).unwrap();
        }
        if (origin instanceof ExprNode) {
            this.ruleId = ((ExprNode) origin).getExpression();
            //用于表达式引用
            ((List)this.computeIfAbsent("$rules", k -> new ArrayList<String>())).add(ruleId);
        }
        return visitor.visit(node);
    }

    @Override
    public void mark(OpFlag flag, ActionNode node) {
        visitor.mark(flag, node);
    }
}








