package com.skyfalling.mousika.eval.visitor;

import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.result.EvalResult;
import com.skyfalling.mousika.eval.result.RuleResult;

import java.util.List;

/**
 * 规则节点访问接口定义
 *
 * @author liyifei
 */
public interface RuleVisitor extends RuleContext {

    /**
     * 评估叶子规则
     */
    EvalResult eval(String ruleId);

    /**
     * 执行规则节点
     */
    EvalResult visit(RuleNode node);

    /**
     * 获取执行结果
     */
    List<RuleResult> getRuleResults();


    /**
     * 获取当前评估节点
     *
     * @return
     */
    EvalNode getCurrentEval();

    /**
     * 设置当前评估节点
     *
     * @param node
     */
    void setCurrentEval(EvalNode node);


}