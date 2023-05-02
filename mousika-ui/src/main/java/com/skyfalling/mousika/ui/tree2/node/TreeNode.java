package com.skyfalling.mousika.ui.tree2.node;

import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.ui.tree2.UINodeAdapter;
import com.skyfalling.mousika.ui.tree2.node.flow.ANode;

/**
 * UI树定义
 * Created on 2023/4/27
 *
 * @author liyifei
 */
public class TreeNode extends ANode {

    public TreeNode() {
        super("");
        this.setNext(ANode.NOP);
    }

    private static UINodeAdapter adapter = new UINodeAdapter();


    public RuleNode toRule() {
        return adapter.toRule(getNext());
    }

    public TreeNode fromRule(RuleNode ruleNode) {
        this.setNext(adapter.fromRule(ruleNode));
        return this;
    }


}
