package com.skyfalling.mousika.ui.tree.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.skyfalling.mousika.ui.tree.node.define.IRNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 规则节点{@link IRNode}实现
 *
 * @author liyifei
 * 
 * Created on 2022-07-19
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RNode<T extends RNode> implements IRNode<T> {

    /**
     * 是否取反
     */
    @Getter
    @Setter
    private boolean negative;

    /**
     * 表达式
     */
    private String expr;

    @JsonCreator
    public RNode(String expr) {
        this.expr = expr;
    }

    @Override
    public String originExpr() {
        return expr;
    }

    public static RNode of(String expr) {
        return new RNode(expr);
    }

    public static RNode of(String expr, boolean negative) {
        RNode rNode = new RNode(expr);
        rNode.setNegative(negative);
        return rNode;
    }
}
