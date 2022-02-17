package com.skyfalling.mousika.eval.action;

import com.skyfalling.mousika.eval.ActionResult;
import com.skyfalling.mousika.eval.EvalResult;
import com.skyfalling.mousika.eval.RuleChecker;
import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.node.RuleNode;

/**
 * 根据条件匹配执行相应业务逻辑
 * Created on 2021/12/14
 *
 * @author liyifei
 */

public class RuleAction implements Action {

    public static final RuleAction TRUE_ACTION = action("true");
    public static final RuleAction FALSE_ACTION = action("false");

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
     * @param ruleNode    规则节点
     * @param trueAction  规则匹配时动作
     * @param falseAction 规则不匹配时动作
     */
    public RuleAction(RuleNode ruleNode, Action trueAction, Action falseAction) {
        this.ruleNode = ruleNode;
        this.trueAction = trueAction;
        this.falseAction = falseAction;
    }


    public static RuleAction action(String ruleExpr) {
        return action(ruleExpr, null, null);
    }

    public static RuleAction action(String ruleExpr, Action trueAction) {
        return action(ruleExpr, trueAction, null);
    }

    public static RuleAction action(String ruleExpr, Action trueAction, Action falseAction) {
        return new RuleAction(RuleChecker.parse(ruleExpr), trueAction, falseAction);
    }


    @Override
    public ActionResult execute(RuleContext context) {
        EvalResult evalResult = context.visit(ruleNode);
        boolean matched = evalResult.isMatched();
        //判断下一步action, 如果trueAction&falseAction都为空,则默认action为返回评估结果
        int flag = (matched && trueAction == null && falseAction != null || !matched && falseAction == null && trueAction != null) ? -1 : matched ? 1 : 0;
        context.reset(flag);
        //如果trueAction&falseAction都为空,则默认action为评估结果
        if (trueAction == null && falseAction == null) {
            return new ActionResult(evalResult.getResult(), context.getEvalResults());
        }
        //规则通过时的trueAction
        if (matched && trueAction != null) {
            return trueAction.execute(context);
        }
        //规则未通过时的falseAction
        if (!matched && falseAction != null) {
            return falseAction.execute(context);
        }
        return ActionResult.NO_RESULT;
    }


}
