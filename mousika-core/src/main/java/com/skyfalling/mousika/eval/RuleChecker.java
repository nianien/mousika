package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.node.ActionNode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.skyfalling.mousika.eval.ActionBuilder.build;


/**
 * 规则集校验器<br/>
 * 给定一个规则集合以及校验对象,返回规则集合的校验结果
 *
 * @author liyifei
 */
@Slf4j
public class RuleChecker {

    /**
     * 规则执行引擎
     */
    private final RuleEngine ruleEngine;


    /**
     * @param ruleEngine 规则执行引擎
     */
    public RuleChecker(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }


    /**
     * 校验业务场景
     *
     * @param actions
     * @param data
     * @return
     */
    public ActionResult check(List<ActionNode> actions, Object data) {
        RuleContext ruleContext = new RuleContextImpl(ruleEngine, data);
        for (ActionNode ruleAction : actions) {
            ActionResult actionResult = ruleAction.eval(ruleContext);
            if (!(actionResult instanceof NaResult)) {
                return actionResult;
            }
        }
        return NaResult.DEFAULT;

    }

    /**
     * 校验规则表达式
     *
     * @param ruleExpr 规则ID表达式,如(1||2)&&(3||!4)
     * @param data
     * @return
     */
    public ActionResult check(String ruleExpr, Object data) {
        return build(ruleExpr).eval(new RuleContextImpl(ruleEngine, data));
    }


}
