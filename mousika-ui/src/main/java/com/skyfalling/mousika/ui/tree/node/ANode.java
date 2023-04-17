package com.skyfalling.mousika.ui.tree.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.skyfalling.mousika.ui.tree.node.define.IANode;
import com.skyfalling.mousika.ui.tree.node.define.IFNode;
import com.skyfalling.mousika.ui.tree.node.define.TypeNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 动作节点{@link IANode}实现
 *
 * @author liyifei
 * Created on 2022-07-19
 */
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ANode<T extends ANode> implements IANode<T>, TypeNode {

    /**
     * 节点表达式
     */
    private String expr;

    /**
     * 后续节点
     */
    @Getter
    private final List<IFNode> flows = new ArrayList<>();

    @JsonCreator(mode = Mode.PROPERTIES)
    public ANode(String expr) {
        this.expr = expr;
    }

    /**
     * 动作节点
     *
     * @param expr
     * @return
     */
    public static ANode of(String expr) {
        return new ANode(expr);
    }

    @Override
    public String originExpr() {
        return expr;
    }


}
