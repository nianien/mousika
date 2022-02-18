package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.eval.listener.ListenerProvider;
import com.skyfalling.mousika.eval.listener.RuleEvent;
import com.skyfalling.mousika.eval.listener.RuleEvent.EventType;
import com.skyfalling.mousika.eval.node.*;
import com.skyfalling.mousika.eval.parser.NodeParser;
import com.skyfalling.mousika.exception.RuleParseException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 构建ActionBuilder
 * Created on 2022/2/18
 *
 * @author liyifei
 */
public class ActionBuilder {

    /**
     * 缓存规则集解析结果
     */
    private static Map<String, RuleNode> nodeCache = new ConcurrentHashMap<>();

    private static Function<String, RuleNode> defaultGenerator = expr -> expr.equals("null") ? NopNode.SINGLETON : new NodeWrapper(new ExprNode(expr));


    /**
     * @param expr condition表达式
     * @return
     */
    public static ActionNode build(String expr) {
        return cast(parse(expr));
    }

    /**
     * @param expr condition节点
     * @param lhs  trueAction节点
     * @return
     */
    public static ActionNode build(String expr, String lhs) {
        return build(parse(expr), parse(lhs), null);
    }


    /**
     * @param expr condition节点
     * @param lhs  trueAction节点
     * @return
     */
    public static ActionNode build(String expr, RuleNode lhs) {
        return build(parse(expr), lhs, null);

    }


    /**
     * @param expr condition表达式
     * @param lhs  trueAction表达式
     * @param rhs  falseAction表达式
     * @return
     */
    public static ActionNode build(String expr, String lhs, String rhs) {
        return build(parse(expr), parse(lhs), parse(rhs));
    }


    /**
     * @param expr condition表达式
     * @param lhs  trueAction表达式
     * @param rhs  falseAction节点
     * @return
     */
    public static ActionNode build(String expr, String lhs, RuleNode rhs) {
        return build(parse(expr), parse(lhs), rhs);
    }

    /**
     * @param expr condition表达式
     * @param lhs  trueAction节点
     * @param rhs  falseAction节点
     * @return
     */
    public static ActionNode build(String expr, RuleNode lhs, String rhs) {
        return build(parse(expr), lhs, parse(rhs));
    }

    /**
     * @param expr condition表达式
     * @param lhs  trueAction节点
     * @param rhs  falseAction节点
     * @return
     */
    public static ActionNode build(String expr, RuleNode lhs, RuleNode rhs) {
        return build(parse(expr), lhs, rhs);
    }


    /**
     * @param expr condition节点
     * @param lhs  trueAction节点
     * @return
     */
    public static ActionNode build(RuleNode expr, RuleNode lhs) {
        return build(expr, lhs, null);
    }


    /**
     * @param expr condition节点
     * @param lhs  trueAction节点
     * @param rhs  falseAction节点
     * @return
     */
    public static ActionNode build(RuleNode expr, RuleNode lhs, RuleNode rhs) {
        if (expr instanceof BoolNode) {
            return new ActionNode((BoolNode) expr, cast(lhs), cast(rhs));
        }
        throw new UnsupportedOperationException("condition node must be a BoolNode: " + expr);
    }


    /**
     * 解析并缓存表达式
     *
     * @param expr
     * @return
     */
    private static RuleNode parse(String expr) {
        return nodeCache.computeIfAbsent(expr, ruleExpr -> {
            try {
                RuleNode ruleNode = NodeParser.parse(ruleExpr, defaultGenerator);
                ListenerProvider.DEFAULT.onParse(new RuleEvent(EventType.PARSE_SUCCEED, ruleExpr, ruleNode));
                return ruleNode;
            } catch (Exception e) {
                ListenerProvider.DEFAULT.onParse(new RuleEvent(EventType.PARSE_FAIL, ruleExpr, e));
                throw new RuleParseException(ruleExpr, "rule parse failed:" + ruleExpr, e);
            }
        });
    }

    /**
     * 转换ActionNode
     *
     * @param node
     * @return
     */
    private static ActionNode cast(RuleNode node) {
        if (node == null || node instanceof NopNode) {
            return null;
        }
        if (node instanceof ActionNode) {
            return (ActionNode) node;
        }
        if (node instanceof BoolNode) {
            return new ActionNode((BoolNode) node);
        }
        throw new UnsupportedOperationException("node must be a ActionNode or BoolNode: " + node);
    }
}
