package com.skyfalling.mousika.ui.tree.visitor;

import com.cudrania.core.utils.StringUtils;
import com.skyfalling.mousika.ui.tree.node.ANode;
import com.skyfalling.mousika.ui.tree.node.FNode;
import com.skyfalling.mousika.ui.tree.node.TreeNode;
import com.skyfalling.mousika.ui.tree.node.ZNode;
import com.skyfalling.mousika.ui.tree.node.define.ILNode;
import com.skyfalling.mousika.ui.tree.node.define.IRNode;
import com.skyfalling.mousika.ui.tree.node.define.TypeNode;

import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Created on 2022/8/3
 *
 * @author liyifei
 */
public interface TreeVisitor {


    /**
     * 规则ID收集
     */
    BiConsumer<TypeNode, Set<String>> RULE_ID_COLLECTOR =
            (n, r) ->

            {
                if (n instanceof IRNode || n instanceof FNode || n instanceof ANode) {
                    if (n.originExpr().matches("\\d+")) {
                        r.add(n.originExpr());
                    }
                }
            };


    /**
     * 节点校验
     */
    BiConsumer<TypeNode, ?> NODE_VALIDATOR = (n, c) -> {
        if (n == null || n instanceof TreeNode) {
            return;
        }
        String originExpr = n.originExpr();
        if (!(n instanceof ZNode) && StringUtils.isEmpty(originExpr)) {
            throw new IllegalStateException("node's expr cannot be empty:" + n.getClass().getSimpleName());
        }
        if (n instanceof ILNode && !originExpr.equals("&&") && !originExpr.equals("||")) {
            throw new IllegalStateException("LNode's expr must be \"&&\" or \"||\": " + originExpr);
        }
    };
}
