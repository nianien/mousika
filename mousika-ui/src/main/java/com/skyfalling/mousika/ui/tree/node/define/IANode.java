package com.skyfalling.mousika.ui.tree.node.define;

import java.util.List;

/**
 * 动作节点, 本身是一个流程节点, 同时包含后续流程节点{@link IFNode}
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 * Created on 2022-07-19
 */
public interface IANode<T extends IANode> extends TypeNode {
    /**
     * 获取后续流程节点
     */
    List<IFNode> getFlows();


    /**
     * 添加后续流程节点
     *
     * @param rNode
     * @return
     */
    default T addFlow(IFNode rNode) {
        this.getFlows().add(rNode);
        return (T) this;
    }
}