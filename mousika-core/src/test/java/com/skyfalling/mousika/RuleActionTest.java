package com.skyfalling.mousika;

import com.skyfalling.mousika.bean.User;
import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.*;
import com.skyfalling.mousika.eval.listener.ListenerProvider;
import com.skyfalling.mousika.eval.listener.RuleEvent;
import com.skyfalling.mousika.eval.listener.RuleListener;
import com.skyfalling.mousika.eval.node.ActionNode;
import com.skyfalling.mousika.suite.RuleScenario;
import com.skyfalling.mousika.suite.RuleSuite;
import com.skyfalling.mousika.udf.*;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.skyfalling.mousika.eval.ActionBuilder.build;
import static org.junit.Assert.assertEquals;


public class RuleActionTest {

    @BeforeClass
    public static void setUp() {

        ListenerProvider.DEFAULT.register(new RuleListener() {
            @Override
            public void onParse(RuleEvent event) {
//                System.out.println(event);
            }

            @Override
            public void onEval(RuleEvent event) {
//                System.out.println(event);
            }
        });
    }

    @Test
    public void testRuleAction() {
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
        ActionNode action1 = build("1&&2?actionA:!3&&4?actionB:actionC");

        String expr = "(1&&2) ? actionA : (!3&&4) ? actionB : actionC";
        String res = "ActionResult{result=null, details=[[RuleResult(expr=\"(1&&2)\",ruleId=2, matched=false, desc='规则2')], [RuleResult(expr=\"(!3&&4)\",ruleId=3, matched=false, desc='规则3'), RuleResult(expr=\"(!3&&4)\",ruleId=4, matched=true, desc='规则4')], [RuleResult(expr=\"actionB\",ruleId=actionB, matched=false, desc='业务操作B')]]}";
        System.out.println(action1);
        assertEquals(expr, action1.toString());
        ActionResult result = action1.eval(context);
        System.out.println(result);
        assertEquals(res, result.toString());

        ActionNode action2 = build("1&&2", "actionA",
                build("!3&&4", "actionB", "actionC")
        );
        System.out.println(action2);
        assertEquals(expr, action2.toString());
        ActionResult result2 = action2.eval(context);
        System.out.println(result2);
        assertEquals(res, result2.toString());
    }


    @SneakyThrows
    @Test
    public void testRuleScenario() {
        RuleDefinition c1 = new RuleDefinition("c1", "false", "业务分支1");
        RuleDefinition c2 = new RuleDefinition("c2", "true", "业务分支2");

        RuleDefinition f1 = new RuleDefinition("101", "isAdult($.name,$.age,$$);", "{$.name}的年龄({$.age})小于{$$.minAge}岁");
        RuleDefinition f3 = new RuleDefinition("102", "$.name!='' && $.age>18", "{$.name}的年龄小于18");
        RuleDefinition f2 = new RuleDefinition("103", "isAdmin($.name,$$)", "用户【{$.name}】不是管理员用户【{$$.admin}】");
        RuleDefinition f4 = new RuleDefinition("104",
                "var udf= Java.type('com.skyfalling.mousika.udf.AdultValidateUdf'); new udf(18).apply($.name,$.age,$$)", "用户【{$.name}】的年龄不满{$$.minAge}岁");

        UdfDefinition udf2 = new UdfDefinition("isAdult", new AdultValidateUdf(18));
        UdfDefinition udf1 = new UdfDefinition("isAdmin", new SystemAdminUdf("system"));
        User root = new User("jack", 17);

        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(c1, c2, f1, f2, f3, f4),
                Arrays.asList(udf1, udf2),
                Arrays.asList(
                        new RuleScenario("sc1", Arrays.asList(
                                build("c1", "!101&&!102"),
                                build("c2", "!103&&104"))
                        ),
                        new RuleScenario("sc2", Arrays.asList("c1?!101&&!102?true:false:null", "c2?!103&&104:null").stream()
                                .map(ActionBuilder::build)
                                .collect(Collectors.toList())
                        )
                ));

        RuleSuite suite = simpleRuleLoader.get();
        String expected = "ActionResult{result=false, details=[[RuleResult(expr=\"c2\",ruleId=c2, matched=true, desc='业务分支2')], [RuleResult(expr=\"(!103&&104)\",ruleId=104, matched=false, desc='用户【jack】的年龄不满18岁')]]}";
        String res1 = suite.checkScenario("sc1", root).toString();
        System.out.println(suite.checkScenario("sc1", root));
        assertEquals(expected, res1);
        String res2 = suite.checkScenario("sc2", root).toString();
        System.out.println(res2);
        assertEquals(expected, res2);
    }


    @SneakyThrows
    @Test
    public void testRule1_0() {
        RuleEngine ruleEngine = new RuleEngine();
        RuleDefinition f1 = new RuleDefinition("1", "false", "规则1");
        RuleDefinition f2 = new RuleDefinition("2", "true", "规则2");
        RuleDefinition f3 = new RuleDefinition("3", "false", "规则3");
        RuleDefinition f4 = new RuleDefinition("4", "false", "规则4");
        RuleDefinition f5 = new RuleDefinition("5", "true", "规则5");
        RuleDefinition f6 = new RuleDefinition("6", "true", "规则6");
        ruleEngine.register(f1);
        ruleEngine.register(f2);
        ruleEngine.register(f3);
        ruleEngine.register(f4);
        ruleEngine.register(f5);
        ruleEngine.register(f6);
        RuleChecker ruleChecker = new RuleChecker(ruleEngine);
        String statement = "(1||2)&&(3||!(4||(5&&6)))";
        User root = new User("jack", 19);
        ActionResult check = ruleChecker.check(statement, root);
        String res = "ActionResult{result=false, details=[[RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6))))\",ruleId=3, matched=false, desc='规则3'), RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6))))\",ruleId=5, matched=true, desc='规则5'), RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6))))\",ruleId=6, matched=true, desc='规则6')]]}";
        System.out.println(check);
        assertEquals(res, check.toString());
    }

    @SneakyThrows
    @Test
    public void testRule2_0() {
        RuleDefinition c1 = new RuleDefinition("c1", "true", "条件1");
        RuleDefinition f1 = new RuleDefinition("1", "false", "规则1");
        RuleDefinition f2 = new RuleDefinition("2", "true", "规则2");
        RuleDefinition f3 = new RuleDefinition("3", "false", "规则3");
        RuleDefinition f4 = new RuleDefinition("4", "false", "规则4");
        RuleDefinition f5 = new RuleDefinition("5", "true", "规则5");
        RuleDefinition f6 = new RuleDefinition("6", "true", "规则6");

        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(c1, f1, f2, f3, f4, f5, f6),
                Arrays.asList(),
                Arrays.asList(new RuleScenario("demo", Arrays.asList(
                                build("c1?(1||2)&&(3||!(4||(5&&6)))?true:false:null"))
                        )
                ));
        RuleSuite suite = simpleRuleLoader.get();
        User context = new User("jack", 19);
        ActionResult check = suite.checkScenario("demo", context);
        String res = "ActionResult{result=false, details=[[RuleResult(expr=\"c1\",ruleId=c1, matched=true, desc='条件1')], [RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6))))\",ruleId=3, matched=false, desc='规则3'), RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6))))\",ruleId=5, matched=true, desc='规则5'), RuleResult(expr=\"((1||2)&&(3||!(4||(5&&6))))\",ruleId=6, matched=true, desc='规则6')], [RuleResult(expr=\"false\",ruleId=false, matched=false, desc='FAILED')]]}";
        System.out.println(check);
        assertEquals(res, check.toString());
    }


}
