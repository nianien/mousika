package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

/**
 * 语法树节点定义
 *
 * @author liyifei
 */
public interface Node<T> {


    /**
     * 规则计算
     *
     * @return
     */
    T eval(RuleContext context);
}
