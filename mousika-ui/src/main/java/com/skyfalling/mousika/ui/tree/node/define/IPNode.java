package com.skyfalling.mousika.ui.tree.node.define;


import java.util.List;

/**
 * 条件分支节点, 包含匹配该分支的动作节点{@link IANode}和用于进一步判断的子分支列表
 *
 * @author liyifei
 * Created on 2022-07-19
 */
public interface IPNode<T extends IPNode> extends TypeNode {

    /**
     * 分支节点
     */
    List<IPNode> getBranches();


    /**
     * 添加分支节点
     *
     * @param ipNode
     * @return
     */
    default T addBranch(IPNode ipNode) {
        this.getBranches().add(ipNode);
        return (T) this;
    }


    /**
     * 获取动作
     */
    IANode getAction();

    /**
     * 设置动作
     */
    void setAction(IANode aNode);
}
