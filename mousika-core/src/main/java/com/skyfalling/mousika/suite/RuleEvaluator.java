package com.skyfalling.mousika.suite;

import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.context.RuleContext;
import com.skyfalling.mousika.eval.RuleVisitor;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.result.EvalResult;
import com.skyfalling.mousika.eval.result.NodeResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

import static com.skyfalling.mousika.eval.parser.NodeBuilder.build;


/**
 * 规则执行器<br>
 * 给定一个规则集合以及校验对象,返回规则集合的校验结果
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
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
     * @param ruleExpr 规则ID表达式,如{@literal (1||2)&&(3||!4)}
     * @param data     用于规则计算的数据对象
     * @return
     */
    public NodeResult eval(String ruleExpr, Object data) {
        return eval(build(ruleExpr), data);
    }

    /**
     * 评估规则节点
     *
     * @param ruleNode 规则节点
     * @param data     用于规则计算的数据对象
     * @return
     */
    public NodeResult eval(RuleNode ruleNode, Object data) {
        return doEval(ruleNode, new RuleVisitor(ruleEngine, data), true);
    }

    /**
     * 评估规则节点
     *
     * @param ruleNode 规则节点
     * @param data     用于规则计算的数据对象
     * @param context  用于规则计算的附加上下文
     * @return
     */
    public NodeResult eval(RuleNode ruleNode, Object data, Map<String, Object> context) {
        RuleVisitor ruleContext = new RuleVisitor(ruleEngine, data);
        ruleContext.putAll(context);
        NodeResult result = doEval(ruleNode, ruleContext, false);
        context.putAll(ruleContext);
        return result;
    }

    /**
     * 节点评估
     *
     * @param ruleNode      规则节点
     * @param context       规则上下文
     * @param includeDetail 是否获取执行详情
     * @return
     */
    private NodeResult doEval(RuleNode ruleNode, RuleContext context, boolean includeDetail) {
        EvalResult evalResult = context.visit(ruleNode);
        // NaResult from expr("null")
        if (evalResult.getResult() instanceof NodeResult) {
            return (NodeResult) evalResult.getResult();
        }
        return new NodeResult(ruleNode.expr(), evalResult.getResult(), includeDetail ? context.getRuleResults() : Collections.emptyList());
    }
}
