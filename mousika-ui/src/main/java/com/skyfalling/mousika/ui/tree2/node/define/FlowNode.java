package com.skyfalling.mousika.ui.tree2.node.define;

/**
 * Flow流式节点
 * <pre>
 *     fN
 *      |
 *     fN
 * </pre>
 *
 * @author liyifei
 * Created on 2022-07-19
 */
public abstract class FlowNode extends UINode implements TypeNode {

    public FlowNode(String expr) {
        super(expr);
    }
}