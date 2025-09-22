package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.context.RuleContext;
import com.skyfalling.mousika.eval.result.EvalResult;
import lombok.Data;

/**
 * 条件执行节点
 * Created on 2023/3/28
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
@Data
public class CaseNode implements RuleNode {
    /**
     * 条件节点
     */
    private RuleNode condition;
    /**
     * 为真节点
     */
    private RuleNode trueCase;

    /**
     * 为假节点
     */
    private RuleNode falseCase;


    /**
     * @param condition 条件节点
     * @param trueCase  为真节点
     * @param falseCase 为假节点
     */
    public CaseNode(RuleNode condition, RuleNode trueCase, RuleNode falseCase) {
        this.condition = condition;
        this.trueCase = trueCase;
        this.falseCase = falseCase;
    }

    /**
     * @param condition 条件节点
     * @param trueCase  为真节点
     */
    public CaseNode(RuleNode condition, RuleNode trueCase) {
        this(condition, trueCase, null);
    }


    @Override
    public EvalResult eval(RuleContext context) {
        EvalResult result = null;
        boolean succeed = context.visit(condition).isMatched();
        if (succeed) {
            if (trueCase != null) {
                result = context.visit(trueCase);
            }
        } else {
            if (falseCase != null) {
                result = context.visit(falseCase);
            }
        }
        return result != null ? new EvalResult(expr(), result.getResult(), result.isMatched()) : new EvalResult(expr(), null);
    }

    @Override
    public String expr() {
        if (trueCase != null && falseCase != null) {
            return condition + "?" + trueCase + ":" + falseCase;
        } else if (trueCase != null) {
            return condition + "?" + trueCase;
        } else if (falseCase != null) {
            return condition.not() + "?" + falseCase;
        }
        return condition.expr();
    }

    @Override
    public String toString() {
        return "(" + this.expr() + ")";
    }
}
