package com.skyfalling.mousika;

import com.skyfalling.mousika.bean.User;
import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.ActionResult;
import com.skyfalling.mousika.eval.RuleChecker;
import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.RuleContextImpl;
import com.skyfalling.mousika.eval.action.RuleAction;
import com.skyfalling.mousika.suite.RuleScenario;
import com.skyfalling.mousika.suite.RuleSuite;
import com.skyfalling.mousika.udf.*;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.skyfalling.mousika.eval.action.RuleAction.action;

public class RuleActionTest {


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
        RuleAction ruleAction = action("1&&2",
                action("actionA"),
                action("!3&&4", action("actionB"), action("actionC"))
        );

        System.out.println(ruleAction.execute(context));
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

        RuleDefinition trueAction = new RuleDefinition("trueAction", "true", "开户通过");
        RuleDefinition falseAction = new RuleDefinition("falseAction", "false", "开户拒绝");

        UdfDefinition udf2 = new UdfDefinition("isAdult", new AdultValidateUdf(18));
        UdfDefinition udf1 = new UdfDefinition("isAdmin", new SystemAdminUdf("system"));
        User root = new User("jack", 17);

        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(c1, c2, f1, f2, f3, f4, trueAction, falseAction),
                Arrays.asList(udf1, udf2),
                Arrays.asList(new RuleScenario("demo", Arrays.asList(
                                action("c1",
                                        action("!101&&!102", action("trueAction"), action("falseAction"))
                                ),
                                action("c2",
                                        action("!103&&!104", action("trueAction"), action("falseAction")
                                        )
                                ))
                        )
                ));
        RuleSuite suite = simpleRuleLoader.get();
        System.out.println(suite.checkScenario("demo", root));
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
        System.out.println(check);
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
        RuleDefinition a1 = new RuleDefinition("trueAction", "true", "通过");
        RuleDefinition a2 = new RuleDefinition("falseAction", "false", "未通过");

        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(c1, f1, f2, f3, f4, f5, f6, a1, a2),
                Arrays.asList(),
                Arrays.asList(new RuleScenario("demo", Arrays.asList(
                                action("c1",
                                        action("(1||2)&&(3||!(4||(5&&6)))", action("trueAction"), action("falseAction"))
                                ))
                        )
                ));
        RuleSuite suite = simpleRuleLoader.get();
        User context = new User("jack", 19);
        ActionResult check = suite.checkScenario("demo", context);
        System.out.println(check);
    }


}
