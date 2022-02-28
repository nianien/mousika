package com.skyfalling.mousika.expr;

import com.skyfalling.mousika.eval.EvalResult;
import com.skyfalling.mousika.eval.node.ActionNode;
import com.skyfalling.mousika.eval.node.RuleNode;

/**
 * 规则节点访问接口
 * Created on 2022/2/11
 *
 * @author liyifei
 */
public interface NodeVisitor {

    /**
     * 访问规则节点
     *
     * @param node
     * @return
     */
    EvalResult visit(RuleNode node);


    /**
     * 执行标记,用于多次节点访问的场景
     *
     * @param flag
     * @param node
     */
    void mark(OpFlag flag, ActionNode node);


    /**
     * 重置操作标记
     */
    enum OpFlag {
        /**
         * 标记成功
         */
        SUCCESS,
        /**
         * 标记失败
         */
        FAILED,
        /**
         * 标记无效
         */
        INVALID,
        /**
         * 标记结束
         */
        FINISH
    }
}
