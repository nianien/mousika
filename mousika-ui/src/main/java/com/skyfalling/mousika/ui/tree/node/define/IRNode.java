package com.skyfalling.mousika.ui.tree.node.define;

/**
 * 简单规则节点, 用于条件判断
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 * Created on 2022-07-19
 */
public interface IRNode<T extends IRNode> extends TypeNode {

    /**
     * 设置是否取反
     *
     * @param negative
     */
    void setNegative(boolean negative);

    /**
     * 判断是否取反
     *
     * @return
     */
    boolean isNegative();

    /**
     * 取反
     *
     * @return
     */
    default T negative() {
        this.setNegative(!isNegative());
        return (T) this;
    }

    /**
     * 生成规则表达式
     *
     * @return
     */
    default String ruleExpr() {
        return (isNegative() ? "!" : "") + originExpr();
    }
}
