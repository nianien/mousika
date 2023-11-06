package com.skyfalling.mousika;

import com.skyfalling.mousika.annotation.Udf;
import com.skyfalling.mousika.bean.User;
import com.skyfalling.mousika.bean.User.Contact;
import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.parser.NodeBuilder;
import com.skyfalling.mousika.eval.result.NodeResult;
import com.skyfalling.mousika.suite.RuleEvaluator;
import com.skyfalling.mousika.udf.*;
import com.skyfalling.mousika.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created on 2022-08-26
 */
@Udf
public class UdfGroupTest {


    @Test
    public void testSayHello() {
        RuleEngine engine = new RuleEngine();
        List<RuleDefinition> definitions = Arrays.asList(
                new RuleDefinition("1", "policy.replace($)", "sys say")
        );
        definitions.forEach(engine::register);
        List<UdfDefinition> udfDefinitions = Arrays.asList(
                new UdfDefinition("policy", "replace", new SayHelloUdf())
        );
        udfDefinitions.forEach(engine::register);
        String arg = "China";
        RuleNode decisionNode = NodeBuilder.build("1");
        NodeResult eval = new RuleEvaluator(engine).eval(decisionNode, arg);
        System.out.println(eval);
        assertEquals(eval.toString(),
                "NodeResult(expr=1, result=China, details=[RuleResult(expr=1,result=China,desc='sys say')])");
    }

    @Test
    public void testAutoCastUdf() {

        RuleEngine engine = new RuleEngine();
        List<RuleDefinition> definitions = Arrays.asList(
                new RuleDefinition("1", "sys.user($)", "name of user"),
                new RuleDefinition("2", "sys.user($,'robbin')", "name of user")
        );
        definitions.forEach(engine::register);
        List<UdfDefinition> udfDefinitions = Arrays.asList(
                new UdfDefinition("sys", "user", new AutoCastUdf())
        );
        udfDefinitions.forEach(engine::register);
        User user = new User("jack.ma", 18, new Contact("11111@ks.com", "1111"));
        String json = JsonUtils.toJson(user);
        Map map = JsonUtils.toMap(json, String.class, Object.class);
        System.out.println(map);
        {
            NodeResult result = new RuleEvaluator(engine).eval(NodeBuilder.build("1"), map);
            System.out.println(result);
        }
        {
            NodeResult result = new RuleEvaluator(engine).eval(NodeBuilder.build("2"), map);
            System.out.println(result);
        }

    }

    @Test
    public void testSayHelloWithGeneric() {
        RuleEngine engine = new RuleEngine();
        List<RuleDefinition> definitions = Arrays.asList(
                new RuleDefinition("1", "sys.sayHello($, {\"river\":\"ChangJiang\"})", "sys.helloWithGeneric")
        );
        definitions.forEach(engine::register);
        List<UdfDefinition> udfDefinitions = Arrays.asList(
                new UdfDefinition("sys", "sayHello", new HelloWithGenericUdf())
        );
        udfDefinitions.forEach(engine::register);
        String arg = "China";
        RuleNode decisionNode = NodeBuilder.build("1");
        NodeResult result = new RuleEvaluator(engine).eval(decisionNode, arg);
        System.out.println(result);
        assertEquals(result.toString(),
                "NodeResult(expr=1, result=This is China k:river v:ChangJiang;, " +
                        "details=[RuleResult(expr=1,result=This is China k:river v:ChangJiang;,desc='sys.helloWithGeneric')])");
    }


    @Test
    public void testSayHelloWithGeneric2() {
        RuleEngine engine = new RuleEngine();
        List<RuleDefinition> definitions = Arrays.asList(
                new RuleDefinition("1", "sys.sayHello($, [\"qi\"], {\"river\":\"ChangJiang\"})",
                        "sys.helloWithGeneric")
        );
        definitions.forEach(engine::register);
        List<UdfDefinition> udfDefinitions = Arrays.asList(
                new UdfDefinition("sys", "sayHello", new HelloWithGenericUdf())
        );
        udfDefinitions.forEach(engine::register);
        String arg = "China";
        NodeResult result = new RuleEvaluator(engine).eval(NodeBuilder.build("1"), arg);
        System.out.println(result);
        assertEquals("NodeResult(expr=1, result=This is China Outstanding person : [qi] k:river v:ChangJiang;, " +
                        "details=[RuleResult(expr=1,result=This is China Outstanding person : [qi] k:river v:ChangJiang;,desc='sys.helloWithGeneric')])",
                result.toString());
    }

    @Test
    public void testSaySceneCheck() {
        RuleEngine engine = new RuleEngine();
        List<RuleDefinition> definitions = Arrays.asList(
                new RuleDefinition("1", "sys.checkScene(\"11\", 11, $$, {\"river\":\"ChangJiang\"})", "sys.checkScene")
        );
        definitions.forEach(engine::register);
        List<UdfDefinition> udfDefinitions = Arrays.asList(
                new UdfDefinition("sys", "checkScene", new CheckSceneUdf())
        );
        udfDefinitions.forEach(engine::register);
        String arg = "China";
        NodeResult result = new RuleEvaluator(engine).eval(NodeBuilder.build("1"), arg);
        System.out.println(result);
        assertEquals("NodeResult(expr=1, result=success, details=[RuleResult(expr=1,result=success,desc='sys.checkScene')])",
                result.toString());
    }

    /**
     * 测试嵌套udf
     */
    @Test
    public void testNestedUdf() {
        RuleEngine engine = new RuleEngine();
        List<RuleDefinition> definitions = Arrays.asList(
                new RuleDefinition("1",
                        "policy.invokeUdf($, [\"qi\"], {\"river\":\"ChangJiang\"},sys.sayHello)",
                        "policy nestedHelloUdf"));
        definitions.forEach(engine::register);
        List<UdfDefinition> udfDefinitions = Arrays.asList(
                new UdfDefinition("policy", "invokeUdf", new CallAnotherUdfUdf()),
                new UdfDefinition("sys", "sayHello", new HelloWithGenericUdf())
        );
        udfDefinitions.forEach(engine::register);
        String arg = "China";
        NodeResult result = new RuleEvaluator(engine).eval(NodeBuilder.build("1"), arg);
        System.out.println(result);
    }

}
