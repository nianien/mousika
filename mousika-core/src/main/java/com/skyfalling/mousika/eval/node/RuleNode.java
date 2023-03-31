package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.result.EvalResult;

/**
 * 规则节点定义
 *
 * @author liyifei
 */
public interface RuleNode {


    /**
     * 规则计算
     */
    EvalResult eval(RuleContext context);


    /**
     * 规则表达式
     */
    String expr();

    /**
     * 条件与
     */
    default RuleNode and(RuleNode node) {
        return new AndNode(this, node);
    }

    /**
     * 条件或
     */
    default RuleNode or(RuleNode node) {
        return new OrNode(this, node);
    }

    /**
     * 条件非
     */
    default RuleNode not() {
        return new NotNode(this);
    }


    /**
     * 顺序执行
     */
    default RuleNode next(RuleNode node) {
        return new SerNode(this, node);
    }
}
