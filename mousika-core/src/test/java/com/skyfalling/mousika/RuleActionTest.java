package com.skyfalling.mousika;

import com.skyfalling.mousika.bean.User;
import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.*;
import com.skyfalling.mousika.eval.node.ActionNode;
import com.skyfalling.mousika.suite.RuleScenario;
import com.skyfalling.mousika.suite.RuleSuite;
import com.skyfalling.mousika.udf.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.skyfalling.mousika.eval.ActionBuilder.build;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RuleActionTest {


    @ParameterizedTest
    @CsvSource(
            value = {
                    "1&&2?actionA:!3&&4?actionB:actionC#(1&&2) ? actionA : (!3&&4) ? actionB : actionC#ActionResult{result=null, details=[[RuleResult(expr=\"(1&&2)\",ruleId=2, matched=false, desc='规则2')], [RuleResult(expr=\"(!3&&4)\",ruleId=3, matched=false, desc='规则3'), RuleResult(expr=\"(!3&&4)\",ruleId=4, matched=true, desc='规则4')], [RuleResult(expr=\"actionB\",ruleId=actionB, matched=false, desc='业务操作B')]]}"
            }, delimiter = '#'
    )
    public void testRuleAction(String expr, String expected1, String expected2) {
        RuleEngine ruleEngine = new RuleEngine();
        List<RuleDefinition> ruleDefinitions = Arrays.asList(
                new RuleDefinition("1", "1==1", "规则1"),
                new RuleDefinition("2", "2!=2", "规则2"),
                new RuleDefinition("3", "findAgentType($.name,$$)==21", "规则3"),
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
                new UdfDefinition("findAgentType", new FindAgentTypeUdf())
        );
        for (UdfDefinition udfDefinition : udfDefinitions) {
            ruleEngine.register(udfDefinition);
        }
        User root = new User("jack", 19);
        RuleContext context = new RuleContextImpl(ruleEngine, root);
        ActionNode action1 = build(expr);
        System.out.println(action1);
        assertEquals(expected1, action1.toString());
        ActionResult result = action1.eval(context);
        System.out.println(result);
        assertEquals(expected2, result.toString());

        ActionNode action2 = build(action1.getCondition().expr(), action1.getTrueAction().expr(),
                build(action1.getFalseAction().getCondition().expr(), action1.getFalseAction().getTrueAction(), action1.getFalseAction().getFalseAction())
        );
        System.out.println(action2);
        assertEquals(expected1, action2.toString());
        ActionResult result2 = action2.eval(context);
        System.out.println(result2);
        assertEquals(expected2, result2.toString());
    }


    @ParameterizedTest
    @CsvSource(
            value = {
                    "!101&&!102#!103&&104#ActionResult{result=false, details=[[RuleResult(expr=\"c2\",ruleId=c2, matched=true, desc='业务分支2')], [RuleResult(expr=\"(!103&&104)\",ruleId=104, matched=false, desc='<104:false>用户【jack】的年龄不满18岁')]]}"
            }, delimiter = '#'
    )
    public void testRuleScenario(String expr1, String expr2, String expected) {

        User root = new User("jack", 17);
        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(
                        new RuleDefinition("c1", "false", "业务分支1"),
                        new RuleDefinition("c2", "true", "业务分支2"),
                        new RuleDefinition("101", "isAdult($.name,$.age,$$)", "{$.name}的年龄({$.age})小于{$$.minAge}岁"),
                        new RuleDefinition("102", "$.name!='' && $.age>18", "{$.name}的年龄小于18"),
                        new RuleDefinition("103", "isAdmin($.name,$$)", "用户【{$.name}】不是管理员用户【{$$.admin}】"),
                        new RuleDefinition("104",
                                "var udf= Java.type('" + AdultValidateUdf.class.getName() + "'); new udf(18).apply($.name,$.age,$$)", "<{$$.$ruleId}:{$$.$result}>用户【{$.name}】的年龄不满{$$.minAge}岁")
                ),
                Arrays.asList(
                        new UdfDefinition("isAdult", new AdultValidateUdf(18)),
                        new UdfDefinition("isAdmin", new SystemAdminUdf("system"))
                ),
                Arrays.asList(
                        new RuleScenario("sc1", Arrays.asList(
                                build("c1", expr1),
                                build("c2", expr2))
                        ),
                        new RuleScenario("sc2", Arrays.asList("c1?" + expr1 + "?true:false:null", "c2?" + expr2 + ":null").stream()
                                .map(ActionBuilder::build)
                                .collect(Collectors.toList())
                        )
                ));

        RuleSuite suite = simpleRuleLoader.get();
        String res1 = suite.checkScenario("sc1", root).toString();
        System.out.println(suite.checkScenario("sc1", root));
        assertEquals(expected, res1);
        String res2 = suite.checkScenario("sc2", root).toString();
        System.out.println(res2);
        assertEquals(expected, res2);
    }


    @ParameterizedTest
    @CsvSource(
            value = {"(1||2)&&(3||!(4||(5&&6&&7)))#ActionResult{result=false, details=[[RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6&&7))))\",ruleId=3, matched=false, desc='规则3'), RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6&&7))))\",ruleId=5, matched=true, desc='规则5'), RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6&&7))))\",ruleId=6, matched=true, desc='规则6'), RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6&&7))))\",ruleId=7, matched=true, desc='规则7')]]}"}, delimiter = '#'
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
        RuleChecker ruleChecker = new RuleChecker(ruleEngine);
        User root = new User("jack", 19);
        ActionResult check = ruleChecker.check(expr, root);
        System.out.println(check);
        assertEquals(expected, check.toString());
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "c1?(1||2)&&(3||!(4||(5&&6)))?true:false:null#ActionResult{result=false, details=[[RuleResult(expr=\"c1\",ruleId=c1, matched=true, desc='条件1')], [RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6))))\",ruleId=3, matched=false, desc='规则3'), RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6))))\",ruleId=5, matched=true, desc='规则5'), RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6))))\",ruleId=6, matched=true, desc='规则6')], [RuleResult(expr=\"false\",ruleId=false, matched=false, desc='FAILED')]]}"
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
                Arrays.asList(),
                Arrays.asList(new RuleScenario("demo", Arrays.asList(build(expr)))
                ));
        RuleSuite suite = simpleRuleLoader.get();
        User context = new User("jack", 19);
        ActionResult check = suite.checkScenario("demo", context);
        System.out.println(check);
        assertEquals(expected, check.toString());
    }


}
