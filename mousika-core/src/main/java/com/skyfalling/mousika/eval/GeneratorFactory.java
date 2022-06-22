package com.skyfalling.mousika.eval;

import com.skyfalling.mousika.eval.node.ExprNode;
import com.skyfalling.mousika.eval.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.skyfalling.mousika.eval.parser.NodeParser.parse;

/**
 * Created on 2022/6/17
 *
 * @author liyifei
 */
public class GeneratorFactory {


    /**
     * 支持规则解析
     *
     * @return
     */
    public static Function<String, Node> create() {
        return expr -> new NodeWrapper(new ExprNode(expr));
    }


    /**
     * 支持规则递归解析
     *
     * @param ruleDefinitions
     * @return
     */
    public static Function<String, Node> create(Map<String, String> ruleDefinitions) {
        return new Function<String, Node>() {
            @Override
            public Node apply(String s) {
                return parse(s, expr -> parseRecursively(expr, new ArrayList<>()));
            }

            private Node parseRecursively(String expr, List<String> hits) {
                //expression是一个规则集
                if (ruleDefinitions.containsKey(expr)) {
                    List<String> newHits = new ArrayList<>(hits);
                    newHits.add(expr);
                    return parse(ruleDefinitions.get(expr), s -> {
                        if (hits.contains(s)) {
                            throw new IllegalStateException("circular dependency between ruleDefinitions [" + s + "] and [" + expr + "]");
                        }
                        return parseRecursively(s, newHits);
                    });
                }
                return new NodeWrapper(new ExprNode(expr));
            }
        };
    }

}
