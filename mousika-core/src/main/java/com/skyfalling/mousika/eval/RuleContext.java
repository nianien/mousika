package com.skyfalling.mousika.eval;



import java.util.List;

/**
 * 规则上下文接口定义
 *
 * @author liyifei <liyifei@kuaishou.com>
 */
public interface RuleContext {

    /**
     * 规则评估对象
     */
    Object getData();


    /**
     * 评估规则
     *
     * @param ruleId
     * @return
     */
    EvalResult eval(String ruleId);


    /**
     * 获取导致评估失败的规则<br/>
     *
     * @return
     */
    List<NodeResult> getEvalResults();

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








