package com.skyfalling.mousika.ui.tree2.node.rule;

import com.skyfalling.mousika.ui.tree2.node.IRNode;
import com.skyfalling.mousika.ui.tree2.node.UINode;
import lombok.Getter;
import lombok.Setter;

/**
 * Rule单规则节点
 *
 * @author liyifei
 * Created on 2022-07-19
 */

public class RNode extends UINode implements IRNode {

    /**
     * 是否取反
     */
    @Getter
    @Setter
    private boolean negative;


    public RNode(String expr) {
        super(expr);
    }


    public RNode(String expr, boolean negative) {
        super(expr);
        this.negative = negative;
    }

    @Override
    public String ruleExpr() {
        return (negative ? "!" : "") + getExpr();
    }
}