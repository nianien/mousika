package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.action.RuleAction;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created on 2022/2/17
 *
 * @author liyifei
 */
@Data
@AllArgsConstructor
public class ActionNode implements RuleNode {

    private RuleNode root;
    private RuleNode left;
    private RuleNode right;

    @Override
    public RuleNode and(RuleNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RuleNode or(RuleNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RuleNode not() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean matches(RuleContext context) {
        throw new UnsupportedOperationException();
    }

    public RuleAction toAction() {
        return new RuleAction(root,
                left == null ? null : left instanceof ActionNode ? ((ActionNode) left).toAction() : new RuleAction(left)
                , right == null ? null : right instanceof ActionNode ? ((ActionNode) right).toAction() : new RuleAction(right));
    }

}
