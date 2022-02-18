package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.eval.listener.ListenerProvider;
import com.skyfalling.mousika.eval.listener.RuleEvent;
import com.skyfalling.mousika.eval.listener.RuleEvent.EventType;
import com.skyfalling.mousika.eval.node.ActionNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.node.ExprNode;
import com.skyfalling.mousika.eval.node.Node;
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
    private static Map<String, Node> nodeCache = new ConcurrentHashMap<>();

    private static Function<String, Node> defaultGenerator = expr -> new NodeWrapper(new ExprNode(expr));


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
    public static ActionNode build(String expr, Node lhs) {
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
    public static ActionNode build(String expr, String lhs, Node rhs) {
        return build(parse(expr), parse(lhs), rhs);
    }

    /**
     * @param expr condition表达式
     * @param lhs  trueAction节点
     * @param rhs  falseAction节点
     * @return
     */
    public static ActionNode build(String expr, Node lhs, String rhs) {
        return build(parse(expr), lhs, parse(rhs));
    }

    /**
     * @param expr condition表达式
     * @param lhs  trueAction节点
     * @param rhs  falseAction节点
     * @return
     */
    public static ActionNode build(String expr, Node lhs, Node rhs) {
        return build(parse(expr), lhs, rhs);
    }


    /**
     * @param expr condition节点
     * @param lhs  trueAction节点
     * @return
     */
    public static ActionNode build(Node expr, Node lhs) {
        return build(expr, lhs, null);
    }


    /**
     * @param expr condition节点
     * @param lhs  trueAction节点
     * @param rhs  falseAction节点
     * @return
     */
    public static ActionNode build(Node expr, Node lhs, Node rhs) {
        if (expr instanceof RuleNode) {
            return new ActionNode((RuleNode) expr, cast(lhs), cast(rhs));
        }
        throw new UnsupportedOperationException("condition node must be a RuleNode: " + expr);
    }


    /**
     * 解析并缓存表达式
     *
     * @param expr
     * @return
     */
    private static Node parse(String expr) {
        return nodeCache.computeIfAbsent(expr, ruleExpr -> {
            try {
                Node node = NodeParser.parse(ruleExpr, defaultGenerator);
                ListenerProvider.DEFAULT.onParse(new RuleEvent(EventType.PARSE_SUCCEED, ruleExpr, node));
                return node;
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
    private static ActionNode cast(Node node) {
        if (node == null) {
            return null;
        }
        if (node instanceof ActionNode) {
            return (ActionNode) node;
        }
        if (node instanceof RuleNode) {
            return new ActionNode((RuleNode) node);
        }
        throw new UnsupportedOperationException("node must be a ActionNode or RuleNode: " + node);
    }
}
