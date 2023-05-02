package com.skyfalling.mousika.ui.tree2.node.flow;


import com.skyfalling.mousika.ui.tree2.node.BranchNode;
import com.skyfalling.mousika.ui.tree2.node.FlowNode;
import lombok.Getter;

/**
 * Serial串行节点
 * <pre>
 *      sN
 *     / | \
 *    /  |  \
 *   fN  fN fN
 * </pre>
 *
 * @author liyifei
 * Created on 2022-07-19
 */
@Getter
public class SNode extends BranchNode<FlowNode> {

    public SNode() {
        super("+");
    }
}
