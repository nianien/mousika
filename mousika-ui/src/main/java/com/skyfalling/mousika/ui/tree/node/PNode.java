package com.skyfalling.mousika.ui.tree.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyfalling.mousika.ui.tree.node.define.IANode;
import com.skyfalling.mousika.ui.tree.node.define.IPNode;
import com.skyfalling.mousika.ui.tree.node.define.IRNode;
import com.skyfalling.mousika.ui.tree.node.define.TypeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 分支节点{@link IPNode}实现
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 * <p>
 * Created on 2022-07-19
 */
public class PNode<T extends PNode> implements IPNode<T>, IRNode<T>, TypeNode {


    /**
     * 表达式
     */
    private String expr;

    /**
     * 条件分支
     */
    @Getter
    private final List<IPNode> branches = new ArrayList<>();
    /**
     * 默认动作
     */
    @Getter
    @Setter
    private IANode action;

    /**
     * 是否取反
     */
    @Getter
    @Setter
    private boolean negative;


    @JsonCreator(mode = Mode.PROPERTIES)
    public PNode(@JsonProperty("expr") String expr) {
        this.expr = expr;
    }


    @Override
    public String originExpr() {
        return expr;
    }


    @Override
    public String toString() {
        return ruleExpr();
    }

    public static PNode of(String expr) {
        return new PNode(expr);
    }

    public static PNode of(String expr, boolean negative) {
        PNode pNode = new PNode(expr);
        pNode.setNegative(negative);
        return pNode;
    }

}
