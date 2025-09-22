package com.skyfalling.mousika.ui.tree.node;

import com.skyfalling.mousika.ui.tree.node.define.IANode;
import com.skyfalling.mousika.ui.tree.node.define.ILNode;
import com.skyfalling.mousika.ui.tree.node.define.IPNode;
import com.skyfalling.mousika.ui.tree.node.define.TypeNode;
import com.skyfalling.mousika.ui.tree.visitor.TreeVisitor;
import com.skyfalling.mousika.utils.JsonUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * UI树的定义
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 * Created on 2022-07-19
 */
public class TreeNode extends ANode<TreeNode> {


    public TreeNode() {
        super("");
    }


    /**
     * 创建树
     */
    public static TreeNode create() {
        return new TreeNode();
    }


    /**
     * 自定义反序列化
     */
    public static TreeNode fromJson(String json) {
        return JsonUtils.toBean(json, TreeNode.class);
    }

    /**
     * 遍历树,获取使用的规则ID
     *
     * @return
     */
    public Set<String> collect() {
        return visit(TreeVisitor.RULE_ID_COLLECTOR, new HashSet<>());
    }

    /**
     * 遍历树,获取使用的规则ID
     *
     */
    public void validate() {
        visit(TreeVisitor.NODE_VALIDATOR, null);
    }

    /**
     * 遍历树节点
     *
     * @param consumer
     * @param result
     * @param <T>
     * @return
     */
    public <T> T visit(BiConsumer<TypeNode, T> consumer, T result) {
        return visit(this, consumer, result);
    }


    /**
     * 自定义JSON序列化
     */
    public String toJson() {
        return JsonUtils.toJson(this);
    }

    /**
     * 遍历树节点
     *
     * @param node     树节点
     * @param consumer 节点处理
     * @param result   遍历结果
     * @return
     */
    private static <T> T visit(TypeNode node, BiConsumer<TypeNode, T> consumer, T result) {
        consumer.accept(node, result);
        if (node instanceof IANode) {
            ((IANode<?>) node).getFlows().forEach(e -> visit(e, consumer, result));
        }
        if (node instanceof ILNode) {
            ((ILNode<?>) node).getRules().forEach(e -> visit(e, consumer, result));
        }
        if (node instanceof IPNode) {
            ((IPNode<?>) node).getBranches().forEach(e -> visit(e, consumer, result));
            visit(((IPNode<?>) node).getAction(), consumer, result);
        }
        return result;
    }

}
