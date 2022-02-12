package com.skyfalling.mousika.expr;

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
    boolean visit(RuleNode node);


    /**
     * 重置访问节点
     *
     * @param flag 0保留失败节点, 1保留成功节点, -1不保留节点
     */
    void reset(int flag);


}
