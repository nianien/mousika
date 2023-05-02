package com.skyfalling.mousika.ui.tree2;

import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.ui.tree2.node.FlowNode;
import com.skyfalling.mousika.ui.tree2.node.flow.ANode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UI树定义
 * Created on 2023/4/27
 *
 * @author liyifei
 */
@NoArgsConstructor
public class TreeNode {
    @Getter
    @Setter
    private FlowNode next = new ANode("nop");

    private static UINodeAdapter adapter = new UINodeAdapter();


    public RuleNode toRule() {
        return adapter.toRule(next);
    }

    public TreeNode fromRule(RuleNode ruleNode) {
        this.setNext(adapter.fromRule(ruleNode));
        return this;
    }


}
