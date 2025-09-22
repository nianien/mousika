package com.skyfalling.mousika.ui.tree2.node.flow;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyfalling.mousika.ui.tree2.node.define.FlowNode;
import com.skyfalling.mousika.utils.Constants;
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
 * @author skyfalling {@literal <skyfalling@live.com>}
 * Created on 2022-07-19
 */
public class ANode extends FlowNode {

    public final static ANode NOP = new ANode(Constants.NOP);
    @Getter
    @Setter
    private FlowNode next;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ANode(@JsonProperty("expr") String expr) {
        super(expr);
    }
}