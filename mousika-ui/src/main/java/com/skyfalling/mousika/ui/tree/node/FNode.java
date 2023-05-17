package com.skyfalling.mousika.ui.tree.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyfalling.mousika.ui.tree.node.define.IFNode;
import com.skyfalling.mousika.ui.tree.node.define.TypeNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 流程节点{@link IFNode}实现
 *
 * @author liyifei
 * Created on 2022-07-19
 */
public class FNode implements IFNode, TypeNode {

    /**
     * 节点表达式
     */
    private String expr;


    @JsonCreator(mode = Mode.PROPERTIES)
    public FNode(@JsonProperty("expr") String expr) {
        this.expr = expr;
    }

    /**
     * 动作节点
     */
    public static FNode of(String expr) {
        return new FNode(expr);
    }

    @Override
    public String originExpr() {
        return expr;
    }


}
