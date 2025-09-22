package com.skyfalling.mousika.ui.tree2.node.flow;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyfalling.mousika.ui.tree2.node.define.FlowNode;
import com.skyfalling.mousika.ui.tree2.node.define.IRNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Condition条件节点
 * <pre>
 *     cN
 *      |
 *      |
 *     fN
 * </pre>
 * 条件约束: {@link CNode#action}不可为空
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 * Created on 2022-07-19
 */
@Getter
@Setter
public class CNode extends FlowNode implements IRNode {

    /**
     * 是否取反
     */
    private boolean negative;

    /**
     * 注意: 这里设置默认值是为了在反序列化时区分{@link ANode}和{@link CNode}
     */
    private FlowNode action = ANode.NOP;

    @JsonCreator(mode = Mode.PROPERTIES)
    public CNode(@JsonProperty("expr") String expr) {
        super(expr);
    }

    @Override
    public String ruleExpr() {
        return (negative ? "!" : "") + getExpr();
    }
}