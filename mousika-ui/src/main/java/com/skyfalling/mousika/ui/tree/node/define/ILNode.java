package com.skyfalling.mousika.ui.tree.node.define;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于条件规则组合的逻辑节点,针对{@link IRNode}节点进行逻辑运算
 *
 * @author liyifei
 * Created on 2022-07-19
 */
public interface ILNode<T extends ILNode> extends IRNode<T> {

    /**
     * 规则集合
     *
     * @return
     */
    List<IRNode> getRules();

    /**
     * 添加子规则
     *
     * @param rNode
     * @return
     */
    default T addRule(IRNode rNode) {
        this.getRules().add(rNode);
        return (T) this;
    }

    /**
     * 规则表达式,默认生成逻辑组合表达式
     */
    default String ruleExpr() {
        List<IRNode> list = getRules();
        String ruleExpr = String.join(originExpr(), list.stream()
                .map(TypeNode::ruleExpr)
                .collect(Collectors.toList()));
        return (isNegative() ? "!" : "") + (list.size() > 1 ? "(" + ruleExpr + ")" : ruleExpr);
    }
}
