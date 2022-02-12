package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.expr.DefaultNodeVisitor;
import com.skyfalling.mousika.expr.NodeVisitor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 规则上下文默认实现
 *
 * @author liyifei <liyifei@kuaishou.com>
 */
@Slf4j
@Getter
public class RuleContextImpl extends HashMap<String, Object> implements RuleContext, NodeVisitor {

    /**
     * 执行引擎
     */
    private RuleEngine ruleEngine;
    /**
     * 待匹配对象
     */
    private Object data;
    /**
     * 用于规则结果分析
     */
    private DefaultNodeVisitor visitor = new DefaultNodeVisitor(this);
    /**
     * 缓存评估结果
     */
    private Map<String, EvalResult> evalCache = new LinkedHashMap<>();
    /**
     * 上下文自定义属性
     */
    private Map<String, Object> properties = new HashMap<>();


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
        return evalCache.computeIfAbsent(ruleId, this::eval0);
    }


    /**
     * 规则评估
     *
     * @param ruleId 规则ID
     */
    private EvalResult eval0(String ruleId) {
        return new EvalResult(ruleEngine.evalRule(ruleId, data, this));
    }


    @Override
    public List<NodeResult> getEvalResults() {
        List<NodeResult> results = new ArrayList<>();
        for (String rule : visitor.getEffectiveRules()) {
            String ruleDesc = ruleEngine.evalDesc(rule, data, this);
            results.add(new NodeResult(rule, evalCache.get(rule), ruleDesc));
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
    public boolean visit(RuleNode node) {
        return visitor.visit(node);
    }

    @Override
    public void reset(int flag) {
        this.visitor.reset(flag);
    }


}








