package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

/**
 * 条件节点定义
 *
 * @author liyifei
 */
public interface BoolNode extends RuleNode {

    /**
     * 条件与
     *
     * @param node
     * @return
     */
    BoolNode and(BoolNode node);

    /**
     * 条件或
     *
     * @param node
     * @return
     */
    BoolNode or(BoolNode node);

    /**
     * 条件非
     *
     * @return
     */
    BoolNode not();

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
