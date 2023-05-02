package com.skyfalling.mousika.ui.tree2.node.flow;

import com.skyfalling.mousika.ui.tree2.node.define.BranchNode;
import com.skyfalling.mousika.ui.tree2.node.define.FlowNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Decision决策节点
 * <pre>
 *      dN
 *     / | \
 *    /  |  \
 *   cN  cN  fN
 * </pre>
 * 条件约束: {@link DNode#action}不可为空
 *
 * @author liyifei
 * Created on 2022-07-19
 */
@Getter
@Setter
public class DNode extends BranchNode<CNode> {

    /**
     * 注意: 这里设置默认值是为了在反序列化时区分{@link CNode}和{@link DNode}
     */
    private FlowNode action = ANode.NOP;

    public DNode() {
        super("-");
    }
}