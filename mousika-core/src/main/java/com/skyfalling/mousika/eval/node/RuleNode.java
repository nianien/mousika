package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

/**
 * 条件节点定义
 *
 * @author liyifei
 */
public interface RuleNode<T> {


    /**
     * 规则计算
     *
     * @return
     */
    T eval(RuleContext context);
}
