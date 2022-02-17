package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.eval.action.RuleAction;
import com.skyfalling.mousika.eval.listener.ListenerProvider;
import com.skyfalling.mousika.eval.listener.RuleEvent;
import com.skyfalling.mousika.eval.listener.RuleEvent.EventType;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.parser.NodeParser;
import com.skyfalling.mousika.exception.RuleParseException;
import com.skyfalling.mousika.expr.NodeVisitor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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
     * 缓存规则集解析结果
     */
    private static Map<String, RuleNode> nodeCache = new ConcurrentHashMap<>();


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
    public ActionResult check(List<RuleAction> actions, Object data) {
        RuleContext ruleContext = new RuleContextImpl(ruleEngine, data);
        for (RuleAction ruleAction : actions) {
            ActionResult actionResult = ruleAction.execute(ruleContext);
            if (actionResult.isHasResult()) {
                return actionResult;
            }
        }
        return ActionResult.NO_RESULT;

    }

    /**
     * 校验规则表达式
     *
     * @param ruleExpr 规则表达式,形式为规则ID的逻辑组合,如(1||2)&&(3||!4)
     * @param data
     * @return
     */
    public ActionResult check(String ruleExpr, Object data) {
        return check(ruleExpr, new RuleContextImpl(ruleEngine, data));
    }


    /**
     * 对指定对象进行规则集的校验
     *
     * @param ruleExpr    规则表达式,形式为规则ID的逻辑组合,如(1||2)&&(3||!4)
     * @param ruleContext 待校验对象
     */
    private ActionResult check(String ruleExpr, RuleContext ruleContext) {
        //将规则表达式解析成节点树,非叶结点为逻辑运算符,叶子节点为脚本表达式
        RuleNode node = parse(ruleExpr);
        boolean matched = node.matches(ruleContext);
        if (ruleContext instanceof NodeVisitor) {
            ruleContext.reset(matched ? 1 : 0);
        }
        return new ActionResult(matched, ruleContext.getEvalResults());
    }

    /**
     * 将规则表达式解析成节点树,非叶结点为逻辑运算符,叶子节点为脚本表达式
     *
     * @param ruleExpr 规则集表达式,形式为规则ID的逻辑组合,如(1||2)&&(3||!4)
     */
    public static RuleNode parse(String ruleExpr) {
        RuleNode ruleNode = nodeCache.computeIfAbsent(ruleExpr, RuleChecker::doParse);
        return ruleNode;
    }

    /**
     * 节点解析
     *
     * @param ruleExpr
     * @return
     */
    private static RuleNode doParse(String ruleExpr) {
        try {
            RuleNode ruleNode = NodeParser.parse(ruleExpr);
            ListenerProvider.DEFAULT.onParse(new RuleEvent(EventType.PARSE_SUCCEED, ruleExpr, ruleNode));
            return ruleNode;
        } catch (Exception e) {
            ListenerProvider.DEFAULT.onParse(new RuleEvent(EventType.PARSE_FAIL, ruleExpr, e));
            throw new RuleParseException(ruleExpr, "rule parse failed:" + ruleExpr, e);
        }

    }

}
