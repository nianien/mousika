package com.skyfalling.mousika.expr;

import com.skyfalling.mousika.eval.EvalResult;
import com.skyfalling.mousika.eval.NodeWrapper;
import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.node.ExprNode;
import com.skyfalling.mousika.eval.node.NotNode;
import com.skyfalling.mousika.eval.node.OrNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录规则节点执行记录
 * Created on 2022/2/11
 *
 * @author liyifei
 */
public class DefaultNodeVisitor implements NodeVisitor {

    private RuleContext ruleContext;
    /**
     * 访问标记,表示当前节点是否为真,最终结果是否为真
     */
    private boolean visitFlag = true;
    /**
     * 导致结果通过的规则集合
     */
    private List<String> trueRules = new ArrayList<>();
    /**
     * 导致结果未通过的规则集合
     */
    private List<String> falseRules = new ArrayList<>();

    /**
     * 影响结果的规则集合
     */
    @Getter
    private List<String> effectiveRules = new ArrayList<>();


    /**
     * 指定规则上下文
     *
     * @param ruleContext
     */
    public DefaultNodeVisitor(RuleContext ruleContext) {
        this.ruleContext = ruleContext;
    }

    /**
     * 访问规则节点
     *
     * @param node
     * @return
     */
    @Override
    public EvalResult visit(RuleNode node) {
        if (node instanceof NodeWrapper) {
            node = ((NodeWrapper) node).unwrap();
        }
        //标记节点
        boolean flag = visitFlag(node);
        int falseSize = falseRules.size();
        boolean isExprNode = node instanceof ExprNode;
        EvalResult result = isExprNode ? ruleContext.eval(((ExprNode) node).getExpression()) : new EvalResult(node.matches(ruleContext));
        //当前节点匹配是否成功
        boolean matched = result.isMatched();
        //整体匹配是否成功
        boolean succeed = matched && flag || !matched && !flag;
        if (isExprNode) {
            //记录影响最终结果的叶子节点
            (succeed ? trueRules : falseRules).add(((ExprNode) node).getExpression());
        } else if (node instanceof OrNode && succeed) {
            //对于or节点,如果匹配成功,则移除fail的叶节点
            falseRules = falseRules.subList(0, falseSize);
        }
        //两次执行,还原访问标记
        this.visitFlag(node);
        return result;
    }

    @Override
    public void reset(int flag) {
        if (flag == 1) {
            this.effectiveRules.addAll(trueRules);
        } else if (flag == 0) {
            this.effectiveRules.addAll(falseRules);
        }
        this.trueRules.clear();
        this.falseRules.clear();
        this.visitFlag = true;
    }


    /**
     * 设置当前节点访问标记
     *
     * @param node
     * @return
     */
    private boolean visitFlag(RuleNode node) {
        boolean flag = visitFlag;
        if (node instanceof NotNode) {
            visitFlag = !visitFlag;
        }
        return flag;
    }


}
