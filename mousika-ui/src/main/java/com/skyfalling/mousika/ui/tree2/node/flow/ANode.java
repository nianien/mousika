package com.skyfalling.mousika.ui.tree2.node.flow;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.skyfalling.mousika.ui.tree2.node.FlowNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Action动作节点
 * <pre>
 *     aN
 *      |
 *      |
 *     fN
 * </pre>
 *
 * @author liyifei
 * Created on 2022-07-19
 */
public class ANode extends FlowNode {

    public final static ANode NOP = new ANode(FlowNode.NOP);
    @Getter
    @Setter
    private FlowNode next;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ANode(String expr) {
        super(expr);
    }
}