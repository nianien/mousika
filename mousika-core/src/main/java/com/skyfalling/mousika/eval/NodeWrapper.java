package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.eval.node.AndNode;
import com.skyfalling.mousika.eval.node.NotNode;
import com.skyfalling.mousika.eval.node.OrNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import lombok.Getter;

/**
 * 节点包装类,用于业务逻辑扩展
 *
 * @author liyifei
 */
@Getter
public class NodeWrapper implements RuleNode {
    /**
     * 原始规则节点
     */
    private RuleNode originNode;

    /**
     * 缓存表达式,避免每次计算
     */
    private String expr;
    /**
     * 缓存toString,避免每次计算
     */
    private String toString;

    public NodeWrapper(RuleNode originNode) {
        this.originNode = originNode;
        this.expr = originNode.expr();
        this.toString = originNode.toString();
    }

    @Override
    public RuleNode and(RuleNode node) {
        return wrap(originNode.and(node));
    }

    @Override
    public RuleNode or(RuleNode node) {
        return wrap(originNode.or(node));
    }

    @Override
    public RuleNode not() {
        return wrap(originNode.not());
    }


    @Override
    public boolean matches(RuleContext ruleContext) {
        return ruleContext.visit(originNode).isMatched();
    }

    @Override
    public String expr() {
        return expr;
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * 包装节点
     */
    private NodeWrapper wrap(RuleNode node) {
        if (node instanceof NodeWrapper) {
            return (NodeWrapper) node;
        }
        return new NodeWrapper(node);
    }

    /**
     * 原生节点
     *
     * @return
     */
    public RuleNode unwrap() {
        return this.originNode;
    }


}
