package com.skyfalling.mousika.eval.node;


import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.result.EvalResult;

/**
 * 复合节点
 * Created on 2023/3/30
 *
 * @author liyifei
 */
public class CompositeNode extends ExprNode {

    private RuleNode ruleNode;

    public CompositeNode(String expression, RuleNode ruleNode) {
        super(expression);
        this.ruleNode = ruleNode;
    }

    @Override
    public EvalResult eval(RuleContext context) {
        EvalResult result = ruleNode.eval(context);
        EvalResult evalResult = new EvalResult(this.toString(), result.getResult(), result.isMatched());
        return evalResult;
    }


    @Override
    public String toString() {
        return this.expr() + "[" + ruleNode.expr() + "]";
    }
}
