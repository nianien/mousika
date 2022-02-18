package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.eval.node.AndNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.node.NotNode;
import com.skyfalling.mousika.eval.node.OrNode;
import lombok.Getter;

/**
 * 节点包装类,用于业务逻辑扩展
 *
 * @author liyifei
 */
@Getter
public class NodeWrapper implements RuleNode {
    private RuleNode originNode;

    public NodeWrapper(RuleNode originNode) {
        this.originNode = originNode;
    }

    @Override
    public RuleNode and(RuleNode node) {
        return wrap(new AndNode(this, node));
    }

    @Override
    public RuleNode or(RuleNode node) {
        return wrap(new OrNode(this, node));
    }

    @Override
    public RuleNode not() {
        return wrap(new NotNode(this));
    }


    @Override
    public boolean matches(RuleContext ruleContext) {
        return ruleContext.visit(originNode).isMatched();
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


    @Override
    public String toString() {
        return originNode.toString();
    }
}
