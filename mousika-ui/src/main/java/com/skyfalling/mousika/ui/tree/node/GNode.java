package com.skyfalling.mousika.ui.tree.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.skyfalling.mousika.ui.tree.node.define.ILNode;
import com.skyfalling.mousika.ui.tree.node.define.IPNode;
import com.skyfalling.mousika.ui.tree.node.define.IRNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 复合分支节点, 同时用作分支节点{@link IPNode}和逻辑节点{@link ILNode}
 *
 * @author liyifei
 * Created on 2022-07-19
 */
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GNode extends PNode<GNode> implements IPNode<GNode>, ILNode<GNode> {

    /**
     * 规则集合
     */
    @Getter
    private final List<IRNode> rules = new ArrayList<>();

    @JsonCreator(mode = Mode.PROPERTIES)
    public GNode(String expr) {
        super(expr);
    }

    public static GNode and() {
        return new GNode("&&");
    }

    public static GNode or() {
        return new GNode("||");
    }


}
