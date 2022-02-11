package com.skyfalling.mousika.eval.action;

import com.skyfalling.mousika.eval.ActionResult;
import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.node.NodeParser;
import com.skyfalling.mousika.eval.node.RuleNode;

/**
 * 根据条件匹配执行相应业务逻辑
 * Created on 2021/12/14
 *
 * @author liyifei
 */

public class RuleAction implements Action {

    /**
     * 待匹配的规则
     */
    private RuleNode ruleNode;
    /**
     * 规则匹配时的动作
     */
    private Action trueAction;
    /**
     * 规则不匹配时的动作
     */
    private Action falseAction;


    /**
     * @param ruleExpr 规则表达式
     */
    public RuleAction(String ruleExpr) {
        this(ruleExpr, null, null);
    }

    /**
     * @param ruleExpr   规则表达式
     * @param trueAction 规则匹配时动作
     */
    public RuleAction(String ruleExpr, Action trueAction) {
        this(ruleExpr, trueAction, null);
    }

    /**
     * @param ruleExpr    规则表达式
     * @param trueAction  规则匹配时动作
     * @param falseAction 规则不匹配时动作
     */
    public RuleAction(String ruleExpr, Action trueAction, Action falseAction) {
        this(NodeParser.parse(ruleExpr), trueAction, falseAction);
    }

    public RuleAction(RuleNode ruleNode, Action trueAction, Action falseAction) {
        this.ruleNode = ruleNode;
        this.trueAction = trueAction;
        this.falseAction = falseAction;
    }


    @Override
    public ActionResult execute(RuleContext context) {
        boolean matches = ruleNode.matches(context);
        if (trueAction == null && falseAction == null) {
            return new ActionResult(matches, context.getEvalResults());
        }
        if (matches) {
            if (trueAction != null) {
                return trueAction.execute(context);
            }
        } else {
            if (falseAction != null) {
                return falseAction.execute(context);
            }
        }
        return ActionResult.NO_RESULT;
    }
}
