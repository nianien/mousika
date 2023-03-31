package com.skyfalling.mousika.eval.parser;


import com.skyfalling.mousika.eval.listener.ListenerProvider;
import com.skyfalling.mousika.eval.listener.RuleEvent;
import com.skyfalling.mousika.eval.listener.RuleEvent.EventType;
import com.skyfalling.mousika.eval.node.CaseNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.exception.RuleParseException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 构建决策节点
 * Created on 2022/2/18
 *
 * @author liyifei
 */
public class NodeBuilder {

    /**
     * 缓存规则集解析结果
     */
    private static Map<String, RuleNode> nodeCache = new ConcurrentHashMap<>();

    /**
     * 默认节点生成器
     */
    private static NodeGenerator defaultGenerator = NodeGenerator.create();

    /**
     * 设置节点生成器
     *
     * @param generator
     */
    public static synchronized void setGenerator(NodeGenerator generator) {
        if (generator != null) {
            nodeCache.clear();
            defaultGenerator = generator;
        }
    }

    /**
     * 解析并缓存表达式
     */
    public static RuleNode build(String expr) {
        return nodeCache.computeIfAbsent(expr, ruleExpr -> {
            long begin = System.currentTimeMillis();
            try {
                RuleNode node = NodeParser.parse(ruleExpr, defaultGenerator);
                long end = System.currentTimeMillis();
                ListenerProvider.DEFAULT.onParse(new RuleEvent(EventType.PARSE_SUCCEED, ruleExpr, node, end - begin));
                return node;
            } catch (Exception e) {
                long end = System.currentTimeMillis();
                ListenerProvider.DEFAULT.onParse(new RuleEvent(EventType.PARSE_FAIL, ruleExpr, e,
                        end - begin));
                throw new RuleParseException(ruleExpr, "rule parse failed:" + ruleExpr, e);
            }
        });
    }


    /**
     * @param expr 条件节点
     * @param lhs  左分支节点
     */
    public static RuleNode build(String expr, String lhs) {
        return new CaseNode(build(expr), build(lhs), null);
    }

    /**
     * @param expr 条件节点
     * @param lhs  左分支节点
     * @param rhs  右分支节点
     */
    public static RuleNode build(String expr, String lhs, String rhs) {
        return new CaseNode(build(expr), build(lhs), build(rhs));
    }


}