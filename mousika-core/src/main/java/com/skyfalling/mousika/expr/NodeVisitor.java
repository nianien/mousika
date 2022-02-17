package com.skyfalling.mousika.expr;

import com.skyfalling.mousika.eval.EvalResult;
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
     * 重置访问节点,用于多次节点访问的场景
     *
     * @param flag
     */
    void reset(OpFlag flag);


    /**
     * 重置操作标记
     */
    enum OpFlag {
        DEFAULT, SUCCEED, FAIL, FINISH
    }
}
