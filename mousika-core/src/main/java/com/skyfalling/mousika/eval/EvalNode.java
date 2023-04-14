package com.skyfalling.mousika.eval;

import lombok.Data;

import java.util.List;
import java.util.Vector;

/**
 * 评估节点定义
 */
@Data
public class EvalNode {

    /**
     * 规则表达式
     */
    private final String expr;

    /**
     * 父执行节点
     */
    private EvalNode parent;

    /**
     * 子执行节点<br/>
     * 引擎支持并行,必须线程安全
     */
    private List<EvalNode> children = new Vector<>();

    /**
     * 执行节点
     *
     * @param expr
     */
    public EvalNode(String expr) {
        this.expr = expr;
    }

    /**
     * 添加子节点
     *
     * @param node
     */
    public void add(EvalNode node) {
        this.children.add(node);
    }

    @Override
    public String toString() {
        return expr;
    }
}