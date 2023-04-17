package com.skyfalling.mousika.ui.tree.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.skyfalling.mousika.ui.tree.node.define.ILNode;
import com.skyfalling.mousika.ui.tree.node.define.IRNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 逻辑节点{@link ILNode}实现
 *
 * @author liyifei
 * 
 * Created on 2022-07-19
 */
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LNode<T extends LNode> extends RNode<T> implements ILNode<T> {

    /**
     * 规则组
     */
    @Getter
    private List<IRNode> rules = new ArrayList<>();

    @JsonCreator(mode = Mode.PROPERTIES)
    public LNode(String expr) {
        super(expr);
    }


    public static LNode and() {
        return new LNode("&&");
    }

    public static LNode or() {
        return new LNode("||");
    }

}
