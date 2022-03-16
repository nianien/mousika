package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.ActionResult;
import com.skyfalling.mousika.eval.EvalResult;
import com.skyfalling.mousika.eval.NaResult;
import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.expr.NodeVisitor.OpFlag;
import lombok.Data;

/**
 * 动作节点定义
 * Created on 2022/2/17
 *
 * @author liyifei
 */
@Data
public class ActionNode implements Node<ActionResult> {

    private RuleNode condition;
    private ActionNode trueAction;
    private ActionNode falseAction;


    /**
     * 构造方法
     *
     * @param condition
     */
    public ActionNode(RuleNode condition) {
        this(condition, null, null);
    }

    /**
     * 构造方法
     *
     * @param condition
     * @param trueAction
     * @param falseAction
     */
    public ActionNode(RuleNode condition, ActionNode trueAction, ActionNode falseAction) {
        this.condition = condition;
        this.trueAction = trueAction;
        this.falseAction = falseAction;
    }


    @Override
    public ActionResult eval(RuleContext context) {
        ActionResult result = doEval(context);
        //标记评估结束
        context.mark(OpFlag.FINISH, this);
        return result;
    }

    /**
     * 节点评估
     *
     * @param context
     * @return
     */
    private ActionResult doEval(RuleContext context) {
        EvalResult evalResult = context.visit(condition);
        boolean matched = evalResult.isMatched();
        //如果trueAction&falseAction都为空,则默认condition为评估结果
        if (trueAction == null && falseAction == null) {
            // NaResult from expr("null")
            if (evalResult.getResult() instanceof ActionResult) {
                return (ActionResult) evalResult.getResult();
            }
            //保留评估成功或失败节点
            context.mark(matched ? OpFlag.SUCCESS : OpFlag.FAILED, this);
            return new ActionResult(evalResult.getResult(), context.getEvalResults());
        }
        //规则通过,执行trueAction
        if (matched && trueAction != null) {
            //保留评估成功节点
            context.mark(OpFlag.SUCCESS, this);
            return trueAction.doEval(context);
        }
        //规则未通过,执行falseAction
        if (!matched && falseAction != null) {
            //保留评估失败节点
            context.mark(OpFlag.FAILED, this);
            return falseAction.doEval(context);
        }
        //没有对应的action,评估节点无效
        context.mark(OpFlag.INVALID, this);
        return NaResult.DEFAULT;
    }

    @Override
    public String toString() {
        if (trueAction == null && falseAction == null) {
            return condition.expr();
        }
        return condition + " ? " + trueAction + " : " + falseAction;
    }
}
