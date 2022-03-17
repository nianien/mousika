package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

/**
 * 规则节点定义
 *
 * @author liyifei
 */
public interface RuleNode extends Node {

    /**
     * 条件与
     *
     * @param node
     * @return
     */
    RuleNode and(RuleNode node);

    /**
     * 条件或
     *
     * @param node
     * @return
     */
    RuleNode or(RuleNode node);

    /**
     * 条件非
     *
     * @return
     */
    RuleNode not();

    /**
     * 评估规则是否匹配
     *
     * @return
     */
    boolean matches(RuleContext context);


    @Override
    default Object eval(RuleContext context) {
        return matches(context);
    }

}
