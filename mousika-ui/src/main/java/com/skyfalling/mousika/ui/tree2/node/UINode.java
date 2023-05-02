package com.skyfalling.mousika.ui.tree2.node;

import lombok.Getter;

/**
 * UI节点
 *
 * @author liyifei
 */
@Getter
public abstract class UINode implements TypeNode {

    /**
     * 节点表达式
     */
    private final String expr;

    public UINode(String expr) {
        this.expr = expr;
    }
}