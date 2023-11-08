package com.skyfalling.mousika;

import com.skyfalling.mousika.bean.User;
import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.result.NodeResult;
import com.skyfalling.mousika.mock.SimpleRuleLoader;
import com.skyfalling.mousika.suite.RuleEvaluator;
import com.skyfalling.mousika.suite.RuleSuite;
import com.skyfalling.mousika.suite.SceneDefinition;
import com.skyfalling.mousika.udf.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static com.skyfalling.mousika.eval.parser.NodeBuilder.build;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RuleEvaluatorTest {


    @ParameterizedTest
    @CsvSource(
            value = {
                    "1&&2?actionA:!3&&4?actionB:actionC#(1&&2)?actionA:((!3&&4)?actionB:actionC)#" +
                            "NodeResult(expr=(1&&2)?actionA:((!3&&4)?actionB:actionC), result=jack, details=[" +
                            "RuleResult(expr=(1&&2)?actionA:((!3&&4)?actionB:actionC),result=jack,subRules=[" +
                            "RuleResult(expr=1&&2,result=false,subRules=[" +
                            "RuleResult(expr=1,result=true,desc='规则1'), " +
                            "RuleResult(expr=2,result=false,desc='规则2')]), " +
                            "RuleResult(expr=(!3&&4)?actionB:actionC,result=jack,subRules=[" +
                            "RuleResult(expr=!3&&4,result=true,subRules=[" +
                            "RuleResult(expr=!3,result=true,subRules=[" +
                            "RuleResult(expr=3,result=false,desc='规则3')]), " +
                            "RuleResult(expr=4,result=true,desc='规则4')]), " +
                            "RuleResult(expr=actionB,result=jack,desc='业务操作B')])])])"
            }, delimiter = '#'
    )
    public void testRuleEval(String expr, String expected1, String expected2) {
        RuleEngine ruleEngine = new RuleEngine();
        List<RuleDefinition> ruleDefinitions = Arrays.asList(
                new RuleDefinition("1", "1==1", "规则1"),
                new RuleDefinition("2", "2!=2", "规则2"),
                new RuleDefinition("3", "getUserType($.name,$$)==1", "规则3"),
                new RuleDefinition("4", "true", "规则4"),
                new RuleDefinition("actionA", "dist($.name,$$.owner)", "业务操作A"),
                new RuleDefinition("actionB", "dist($.name,$$.owner);sayHello($.name);", "业务操作B"),
                new RuleDefinition("actionC", "'c'", "业务操作C")
        );
        for (RuleDefinition ruleDefinition : ruleDefinitions) {
            ruleEngine.register(ruleDefinition);
        }
        List<UdfDefinition> udfDefinitions = Arrays.asList(
                new UdfDefinition("dist", new DistributeUdf()),
                new UdfDefinition("sayHello", new SayHelloUdf()),
                new UdfDefinition("getUserType", new GetUserTypeUdf())
        );
        for (UdfDefinition udfDefinition : udfDefinitions) {
            ruleEngine.register(udfDefinition);
        }
        User root = new User("jack", 19);
        RuleEvaluator ruleEvaluator = new RuleEvaluator(ruleEngine);
        RuleNode ruleNode = build(expr);
        System.out.println(ruleNode);
        assertEquals(expected1, ruleNode.expr());
        NodeResult result = ruleEvaluator.eval(ruleNode, root);
        System.out.println(result);
        assertEquals(expected2, result.toString());

    }


    @ParameterizedTest
    @CsvSource(
            value = {
                    "!101&&!102?105:106#!103&&104#" +
                            "NodeResult(expr=c1?((!101&&!102)?105:106):(c2?(!103&&104)), result=false, details=[" +
                            "RuleResult(expr=c1?((!101&&!102)?105:106):(c2?(!103&&104)),result=false,subRules=[" +
                            "RuleResult(expr=c1,result=true,desc='业务分支1'), " +
                            "RuleResult(expr=(!101&&!102)?105:106,result=false,subRules=[" +
                            "RuleResult(expr=!101&&!102,result=true,subRules=[" +
                            "RuleResult(expr=!101,result=true,subRules=[" +
                            "RuleResult(expr=101,result=false,desc='jack的年龄(17)小于18岁')]), " +
                            "RuleResult(expr=!102,result=true,subRules=[" +
                            "RuleResult(expr=102,result=false,desc='jack的年龄小于18')])]), " +
                            "RuleResult(expr=105,result=false,desc='调用场景2')])])])"
            }, delimiter = '#'
    )
    public void testRuleScene1(String expr1, String expr2, String expected) {

        User root = new User("jack", 17);
        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(
                        new RuleDefinition("c1", "true", "业务分支1"),
                        new RuleDefinition("c2", "true", "业务分支2"),
                        new RuleDefinition("101", "isAdult($.name,$.age,$$)", "{$.name}的年龄({$.age})小于{$$.minAge}岁"),
                        new RuleDefinition("102", "$.name!='' && $.age>18", "{$.name}的年龄小于18"),
                        new RuleDefinition("103", "isAdmin($.name,$$)", "用户【{$.name}】不是管理员用户【{$$.admin}】"),
                        new RuleDefinition("104",
                                "var udf= Java.type('" + AdultValidateUdf.class.getName()
                                        + "'); new udf(18).apply($.name,$.age,$$)", "用户【{$.name}】的年龄不满{$$.minAge}岁"),
                        new RuleDefinition("105", "sceneCall('sc2',$,$$)", "调用场景2"),
                        new RuleDefinition("106", "sceneCall('sc2',$,$$)", "用户【{$.name}】不是管理员用户【{$$.admin}】")
                ),
                Arrays.asList(
                        new UdfDefinition("isAdult", new AdultValidateUdf(18)),
                        new UdfDefinition("isAdmin", new SystemAdminUdf("system")),
                        new UdfDefinition("sceneCall", new EvalSceneUdf())
                ), Arrays.asList(
                new SceneDefinition("sc1", "", "c1?" + expr1 + ":c2?" + expr2),
                new SceneDefinition("sc2", "", "102")
                ));

        RuleSuite suite = simpleRuleLoader.loadSuite();
        String res1 = suite.evalScene("sc1", root).toString();
        System.out.println(res1);
        assertEquals(expected, res1);
    }


    @ParameterizedTest
    @CsvSource(
            value = {
                    "!101&&!102#!103&&104#" +
                            "NodeResult(expr=c1?(!101&&!102):(c2?(!103&&104)), result=false, details=[" +
                            "RuleResult(expr=c1?(!101&&!102):(c2?(!103&&104)),result=false,subRules=[" +
                            "RuleResult(expr=c1,result=false,desc='业务分支1'), " +
                            "RuleResult(expr=c2?(!103&&104),result=false,subRules=[" +
                            "RuleResult(expr=c2,result=true,desc='业务分支2'), " +
                            "RuleResult(expr=!103&&104,result=false,subRules=[" +
                            "RuleResult(expr=!103,result=true,subRules=[" +
                            "RuleResult(expr=103,result=false,desc='用户【jack】不是管理员用户【system】')]), " +
                            "RuleResult(expr=104,result=false,desc='用户【jack】的年龄不满18岁')])])])])"
            }, delimiter = '#'
    )
    public void testRuleScene(String expr1, String expr2, String expected) {

        User root = new User("jack", 17);
        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(
                        new RuleDefinition("c1", "false", "业务分支1"),
                        new RuleDefinition("c2", "true", "业务分支2"),
                        new RuleDefinition("101", "isAdult($.name,$.age,$$)", "{$.name}的年龄({$.age})小于{$$.minAge}岁"),
                        new RuleDefinition("102", "$.name!='' && $.age>18", "{$.name}的年龄小于18"),
                        new RuleDefinition("103", "isAdmin($.name,$$)", "用户【{$.name}】不是管理员用户【{$$.admin}】"),
                        new RuleDefinition("104",
                                "var udf= Java.type('" + AdultValidateUdf.class.getName()
                                        + "'); new udf(18).apply($.name,$.age,$$)", "用户【{$.name}】的年龄不满{$$.minAge}岁")
                ),
                Arrays.asList(
                        new UdfDefinition("isAdult", new AdultValidateUdf(18)),
                        new UdfDefinition("isAdmin", new SystemAdminUdf("system"))
                ));

        RuleEvaluator ruleEvaluator = simpleRuleLoader.loadSuite().getRuleEvaluator();
        String res1 = ruleEvaluator.eval("c1?" + expr1 + ":c2?" + expr2, root).toString();
        System.out.println(res1);

        String res2 = ruleEvaluator
                .eval(build("c1?" + expr1 + "?true:false:c2?" + expr2), root).toString();
        System.out.println(res2);
        assertEquals(expected, res1);
        assertEquals(expected.substring(expected.indexOf("subRules")), res2.substring(res2.indexOf("subRules")));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "(1||2)&&(3||!(4||(5&&6&&7)))#" +
                            "NodeResult(expr=(1||2)&&(3||!(4||(5&&6&&7))), result=false, details=[" +
                            "RuleResult(expr=(1||2)&&(3||!(4||(5&&6&&7))),result=false,subRules=[" +
                            "RuleResult(expr=1||2,result=true,subRules=[" +
                            "RuleResult(expr=1,result=false,desc='规则1'), " +
                            "RuleResult(expr=2,result=true,desc='规则2')]), " +
                            "RuleResult(expr=3||!(4||(5&&6&&7)),result=false,subRules=[" +
                            "RuleResult(expr=3,result=false,desc='规则3'), " +
                            "RuleResult(expr=!(4||(5&&6&&7)),result=false,subRules=[" +
                            "RuleResult(expr=4||(5&&6&&7),result=true,subRules=[" +
                            "RuleResult(expr=4,result=false,desc='规则4'), " +
                            "RuleResult(expr=5&&6&&7,result=true,subRules=[" +
                            "RuleResult(expr=5,result=true,desc='规则5'), " +
                            "RuleResult(expr=6,result=true,desc='规则6'), " +
                            "RuleResult(expr=7,result=true,desc='规则7')])])])])])])"},
            delimiter = '#'
    )
    public void testRule1_0(String expr, String expected) {
        RuleEngine ruleEngine = new RuleEngine();
        for (RuleDefinition f : Arrays.asList(
                new RuleDefinition("1", "false", "规则1"),
                new RuleDefinition("2", "true", "规则2"),
                new RuleDefinition("3", "false", "规则3"),
                new RuleDefinition("4", "false", "规则4"),
                new RuleDefinition("5", "true", "规则5"),
                new RuleDefinition("6", "true", "规则6"),
                new RuleDefinition("7", "true", "规则7")
        )) {
            ruleEngine.register(f);
        }
        RuleEvaluator ruleEvaluator = new RuleEvaluator(ruleEngine);
        User root = new User("jack", 19);
        NodeResult check = ruleEvaluator.eval(expr, root);
        System.out.println(check);
        assertEquals(expected, check.toString());
    }


    @ParameterizedTest
    @CsvSource(
            value = {
                    "c1?(1||2)&&(3||!(4||(5&&6)))?true:false:null#" +
                            "NodeResult(expr=c1?(((1||2)&&(3||!(4||(5&&6))))?true:false):null, result=false, details=[" +
                            "RuleResult(expr=c1?(((1||2)&&(3||!(4||(5&&6))))?true:false):null,result=false,subRules=[" +
                            "RuleResult(expr=c1,result=true,desc='条件1'), " +
                            "RuleResult(expr=((1||2)&&(3||!(4||(5&&6))))?true:false,result=false,subRules=[" +
                            "RuleResult(expr=(1||2)&&(3||!(4||(5&&6))),result=false,subRules=[" +
                            "RuleResult(expr=1||2,result=true,subRules=[" +
                            "RuleResult(expr=1,result=false,desc='规则1'), " +
                            "RuleResult(expr=2,result=true,desc='规则2')]), " +
                            "RuleResult(expr=3||!(4||(5&&6)),result=false,subRules=[" +
                            "RuleResult(expr=3,result=false,desc='规则3'), " +
                            "RuleResult(expr=!(4||(5&&6)),result=false,subRules=[" +
                            "RuleResult(expr=4||(5&&6),result=true,subRules=[" +
                            "RuleResult(expr=4,result=false,desc='规则4'), " +
                            "RuleResult(expr=5&&6,result=true,subRules=[" +
                            "RuleResult(expr=5,result=true,desc='规则5'), " +
                            "RuleResult(expr=6,result=true,desc='规则6')])])])])]), " +
                            "RuleResult(expr=false,result=false,desc='FAILED')])])])"
            }, delimiter = '#'
    )
    public void testRule2_0(String expr, String expected) {
        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(
                        new RuleDefinition("c1", "true", "条件1"),
                        new RuleDefinition("1", "false", "规则1"),
                        new RuleDefinition("2", "true", "规则2"),
                        new RuleDefinition("3", "false", "规则3"),
                        new RuleDefinition("4", "false", "规则4"),
                        new RuleDefinition("5", "true", "规则5"),
                        new RuleDefinition("6", "true", "规则6")
                ),
                Arrays.asList());
        RuleEvaluator ruleEvaluator = simpleRuleLoader.loadSuite().getRuleEvaluator();
        User context = new User("jack", 19);
        NodeResult check = ruleEvaluator.eval(expr, context);
        System.out.println(check);
        assertEquals(expected, check.toString());
    }
}
