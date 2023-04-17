package com.skyfalling.mousika.ui.tree.visitor;

import com.cudrania.core.utils.StringUtils;
import com.skyfalling.mousika.ui.tree.node.define.IANode;
import com.skyfalling.mousika.ui.tree.node.define.ILNode;
import com.skyfalling.mousika.ui.tree.node.define.IRNode;
import com.skyfalling.mousika.ui.tree.node.define.TypeNode;
import com.skyfalling.mousika.ui.tree.node.TreeNode;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created on 2022/8/3
 *
 * @author liyifei
 */
public interface NodeVisitors {


    /**
     * 规则ID收集
     */
    Function<TypeNode, String> RULE_ID_COLLECTOR =
            n -> (n instanceof IRNode && !(n instanceof ILNode) || n instanceof IANode) ? n.originExpr() : null;

    /**
     * 规则ID收集并转换成Long型
     */
    Function<TypeNode, Long> RULE_ID_TO_LONG_COLLECTOR = RULE_ID_COLLECTOR
            .andThen(e -> Optional.ofNullable(e).filter(s -> s.matches("\\d+")).map(Long::parseLong).orElse(null));

    /**
     * 节点校验
     */
    Consumer<TypeNode> NODE_VALIDATOR = n -> {
        if (n == null) {
            throw new IllegalStateException("节点不能为空");
        }
        String originExpr = n.originExpr();
        if (!(n instanceof TreeNode) && StringUtils.isEmpty(originExpr)) {
            throw new IllegalStateException("规则表达式不能为空");
        }
        if (n instanceof ILNode && !originExpr.equals("&&") && !originExpr.equals("||")) {
            throw new IllegalStateException("LNode节点表达式只能为\"&&\" 或 \"||\": " + originExpr);
        }
    };
}
