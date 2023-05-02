package com.skyfalling.mousika.ui.tree2.node.flow;


import com.skyfalling.mousika.ui.tree2.node.BranchNode;
import com.skyfalling.mousika.ui.tree2.node.FlowNode;
import lombok.Getter;

/**
 * Parallel并行节点
 * <pre>
 *      pN
 *     / | \
 *    /  |  \
 *   fN  fN fN
 * </pre>
 *
 * @author liyifei
 * Created on 2022-07-19
 */
@Getter
public class PNode extends BranchNode<FlowNode> {

    public PNode() {
        super("=");
    }
}
