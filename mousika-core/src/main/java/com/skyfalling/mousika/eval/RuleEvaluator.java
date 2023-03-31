package com.skyfalling.mousika.eval;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.result.EvalResult;
import com.skyfalling.mousika.eval.result.NodeResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

import static com.skyfalling.mousika.eval.parser.NodeBuilder.build;


/**
 * 规则执行器<br/>
 * 给定一个规则集合以及校验对象,返回规则集合的校验结果
 *
 * @author liyifei
 */
@Slf4j
public class RuleEvaluator {

    /**
     * 规则执行引擎
     */
    private final RuleEngine ruleEngine;

    /**
     * @param ruleEngine 规则执行引擎
     */
    public RuleEvaluator(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }


    /**
     * 评估规则表达式
     *
     * @param ruleExpr 规则ID表达式,如(1||2)&&(3||!4)
     */
    public NodeResult eval(String ruleExpr, Object data) {
        return eval(build(ruleExpr), data);
    }

    /**
     * 评估规则节点
     *
     * @param ruleNode
     * @param data
     * @return
     */
    public NodeResult eval(RuleNode ruleNode, Object data) {
        return doEval(ruleNode, new RuleContextImpl(ruleEngine, data), true);
    }


    /**
     * 节点评估
     *
     * @param ruleNode
     * @param context
     * @param includeDetail 是否获取执行详情
     * @return
     */
    public NodeResult doEval(RuleNode ruleNode, RuleContext context, boolean includeDetail) {
        EvalResult evalResult = context.visit(ruleNode);
        // NaResult from expr("null")
        if (evalResult.getResult() instanceof NodeResult) {
            return (NodeResult) evalResult.getResult();
        }
        return new NodeResult(ruleNode.expr(), evalResult.getResult(), includeDetail ? context.collect() : Collections.emptyList());
    }
}
