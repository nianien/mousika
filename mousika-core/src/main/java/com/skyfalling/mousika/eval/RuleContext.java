package com.skyfalling.mousika.eval;


import com.skyfalling.mousika.expr.NodeVisitor;

import java.util.List;

/**
 * 规则上下文接口定义
 *
 * @author liyifei
 */
public interface RuleContext extends NodeVisitor {

    /**
     * 规则评估对象
     */
    Object getData();

    /**
     * 获取规则描述
     *
     * @return
     */
    String evalDesc(String ruleId);

    /**
     * 评估规则
     *
     * @param ruleId
     * @return
     */
    EvalResult eval(String ruleId);

    /**
     * 获取当前规则
     */
    String getCurrentRule();

    /**
     * 获取导致评估结果对应的规则<br/>
     *
     * @return
     */
    List<List<RuleResult>> getEvalResults();

    /**
     * 获取上下文属性
     *
     * @param name
     * @return
     */
    Object getProperty(Object name);

    /**
     * 添加上下文属性
     *
     * @param name
     * @param value
     * @return
     */
    void setProperty(String name, Object value);
}