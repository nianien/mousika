package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.eval.node.*;
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
        if (originNode instanceof AndNode) {
            return originNode.and(wrap(node));
        }
        return wrap(new AndNode(this, node));
    }

    @Override
    public RuleNode or(RuleNode node) {
        if (originNode instanceof OrNode) {
            return originNode.or(wrap(node));
        }
        return wrap(new OrNode(this, node));
    }

    @Override
    public RuleNode not() {
        if (originNode instanceof NotNode) {
            return wrap(originNode.not());
        }
        return wrap(new NotNode(this));
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
        if (originNode instanceof NodeWrapper) {
            return ((NodeWrapper) originNode).unwrap();
        }
        return this.originNode;
    }


}
