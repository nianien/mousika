package com.skyfalling.mousika.ui.tree.node;

import com.skyfalling.mousika.utils.JsonUtils;

/**
 * UI树的定义
 *
 * @author liyifei
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

//    /**
//     * 遍历树
//     */
//    public <T, C extends Collection<T>> C visit(Function<TypeNode, T> function, Supplier<C> supplier) {
//        return (C) this.visit(this, function, supplier.get());
//    }
//
//    /**
//     * 遍历树
//     */
//    public <T> List<T> visit(Function<TypeNode, T> function) {
//        return this.visit(function, ArrayList::new);
//    }
//
//    /**
//     * 遍历节点
//     */
//    public void visit(Consumer<TypeNode> consumer) {
//        visit(n -> {
//            consumer.accept(n);
//            return null;
//        });
//    }
//
//    /**
//     * 遍历节点
//     */
//    private static <T> Collection<T> visit(TypeNode node, Function<TypeNode, T> function, Collection<T> collector) {
//        T t = function.apply(node);
//        if (t != null) {
//            collector.add(t);
//        }
//        if (node instanceof ILNode) {
//            ((ILNode<?>) node).getRules().forEach(e -> visit(e, function, collector));
//        }
////        if (node instanceof IANode) {
////            ((IANode<?>) node).getBranches().forEach(e -> visit(e, function, collector));
////            visit(((IANode<?>) node).getAction(), function, collector);
////        }
//        return collector;
//    }


    /**
     * 自定义反序列化
     */
    public static TreeNode fromJson(String json) {
        return JsonUtils.toBean(json, TreeNode.class);
    }


    /**
     * 自定义JSON序列化
     */
    public String toJson() {
        return JsonUtils.toJson(this);
    }


}
