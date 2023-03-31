package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.eval.result.EvalResult;
import com.skyfalling.mousika.eval.result.RuleResult;
import com.skyfalling.mousika.expr.NodeVisitor;

import java.util.List;
import java.util.Map;

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
     */
    String evalDesc(String ruleId);

    /**
     * 评估规则
     */
    EvalResult eval(String ruleId);

    /**
     * 获取当前规则
     */
    String getCurrentRule();

    /**
     * 获取导致评估结果对应的规则<br/>
     */
    List<RuleResult> collect();

    /**
     * 获取上下文属性
     */
    Object getProperty(Object name);

    /**
     * 添加上下文属性
     */
    void setProperty(String name, Object value);


    /**
     * 复制当前上下文
     *
     * @param extra 附加信息
     * @return
     * @return
     */
    RuleContext copy(Map<String, Object> extra);
}