package com.skyfalling.mousika.ui.tree.node;

import com.skyfalling.mousika.ui.tree.node.define.IANode;
import com.skyfalling.mousika.ui.tree.node.define.IFNode;
import com.skyfalling.mousika.ui.tree.node.define.IPNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 虚拟流程节点, 同时定义复杂流程, 用作{@link IPNode}节点和{@link IFNode}节点
 *
 * @author liyifei
 * Created on 2022-07-19
 */
@NoArgsConstructor
public class ZNode<T extends ZNode> implements IPNode<T>, IFNode {

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


}
