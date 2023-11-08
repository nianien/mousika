package com.skyfalling.mousika;

import com.skyfalling.mousika.bean.User;
import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.parser.NodeBuilder;
import com.skyfalling.mousika.eval.result.NodeResult;
import com.skyfalling.mousika.suite.RuleEvaluator;
import com.skyfalling.mousika.udf.SayHelloUdf;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created on 2023/3/28
 *
 * @author liyifei
 */
public class RuleNodeFlowTest {


    private RuleEngine ruleEngine;

    {
        List<RuleDefinition> ruleDefinitions = Arrays.asList(
                new RuleDefinition("t1", "true", "为真规则1"),
                new RuleDefinition("t2", "true", "为真规则2"),
                new RuleDefinition("t3", "true", "为真规则3"),
                new RuleDefinition("t4", "true", "为真规则4"),
                new RuleDefinition("f1", "false", "为假规则1"),
                new RuleDefinition("f2", "false", "为假规则2"),
                new RuleDefinition("f3", "false", "为假规则3"),
                new RuleDefinition("f4", "false", "为假规则4"),
                new RuleDefinition("a1", "'a1'", "业务操作1"),
                new RuleDefinition("a2", "'a2'", "业务操作2"),
                new RuleDefinition("a3", "'a3'", "业务操作3"),
                new RuleDefinition("a4", "'a4'", "业务操作4")
        );
        ruleEngine = RuleEngine.builder().ruleDefinitions(ruleDefinitions).build();
    }

    @Test
    public void testNop() {
        User root = new User("jack", 19);
        RuleNode node = NodeBuilder.build("(∅->a1)->(∅->a2->a3)");
        System.out.println(node.expr());
        NodeResult nodeResult = new RuleEvaluator(ruleEngine).eval(node, root);
        System.out.println(nodeResult);
    }


    @ParameterizedTest
    @CsvSource(value = {
            "t1?f3?a1:f4?a2:a3#t1?(f3?a1:(f4?a2:a3))",
            "t1?f3?a1:(f4?a2):a3#t1?(f3?a1:(f4?a2)):a3",
            "f1?t2?t3?a1:a2:a3#f1?(t2?(t3?a1:a2):a3)"
    }, delimiter = '#')
    public void testEval(String expr, String expected) {
        User root = new User("jack", 19);
        RuleNode node = NodeBuilder.build(expr);
        System.out.println(node.expr());
        NodeResult nodeResult = new RuleEvaluator(ruleEngine).eval(node, root);
        System.out.println(nodeResult);
        assertEquals(expected, node.expr());

    }

    @Test
    public void testIfNode() {
        User root = new User("jack", 19);
        RuleNode node = NodeBuilder.build("(t1&&t4?t2:t3)?(a1->a2->t1?a3):(a3->a4)");
        System.out.println(node.toString());
        NodeResult nodeResult = new RuleEvaluator(ruleEngine).eval(node, root);
        System.out.println(nodeResult);

    }


    @Test
    public void tesSerNode() {
        User root = new User("jack", 19);
        RuleNode node = NodeBuilder.build("a1->a2->a3->a4");
        System.out.println(node.toString());
        NodeResult nodeResult = new RuleEvaluator(ruleEngine).eval(node, root);
        System.out.println(nodeResult);
    }

    @SneakyThrows
    @Test
    public void tesParNode() {
        User root = new User("jack", 19);
        RuleNode node = NodeBuilder.build("f1->f2->f3->a1=>a2=>a3=>a4->t1->t2->t3");
        System.out.println(node.toString());
        NodeResult nodeResult = new RuleEvaluator(ruleEngine).eval(node, root);
        System.out.println(nodeResult);
    }

    @Test
    public void tesIfElse() {
        RuleNode actionNode = NodeBuilder.build("a?b:c");
        System.out.println(actionNode.toString());
        RuleEngine.RuleEngineBuilder builder = RuleEngine.builder()
                .ruleDefinitions(Arrays.asList(
                        new RuleDefinition("a", "true", "规则1"),
                        new RuleDefinition("b", "false", "规则2"),
                        new RuleDefinition("c", "sayHello($.name);", "业务操作"),
                        new RuleDefinition("d", "'d'", "规则2")
                ))
                .udfDefinitions(Arrays.asList(
                        new UdfDefinition("sayHello", new SayHelloUdf())
                ));
        User root = new User("jack", 19);
        NodeResult nodeResult = new RuleEvaluator(builder.build()).eval(actionNode, root);
        System.out.println(nodeResult);

    }


}
