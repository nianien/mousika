package com.skyfalling.mousika;

import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.ActionResult;
import com.skyfalling.mousika.eval.RuleChecker;
import com.skyfalling.mousika.suite.RuleScenario;
import com.skyfalling.mousika.suite.RuleSuite;
import com.skyfalling.mousika.udf.AdultValidateUdf;
import com.skyfalling.mousika.udf.SystemAdminUdf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Arrays;

import static com.skyfalling.mousika.eval.action.RuleAction.action;

public class RuleActionTest {


    @SneakyThrows
    @Test
    public void testRuleAction() {
        RuleDefinition c1 = new RuleDefinition("c1", "false", "业务分支1");
        RuleDefinition c2 = new RuleDefinition("c2", "true", "业务分支2");

        RuleDefinition f1 = new RuleDefinition("101", "isAdult($.name,$.age,$$);", "{$.name}的年龄({$.age})小于{$$.minAge}岁");
        RuleDefinition f3 = new RuleDefinition("102", "$.name!='' && $.age>18", "{$.name}的年龄小于18");
        RuleDefinition f2 = new RuleDefinition("103", "isAdmin($.name,$$)", "用户【{$.name}】不是管理员用户【{$$.admin}】");
        RuleDefinition f4 = new RuleDefinition("104",
                "var udf= Java.type('com.skyfalling.mousika.udf.AdultValidateUdf'); new udf(18).apply($.name,$.age,$$)", "{$$.name}的年龄不满18岁");

        RuleDefinition trueAction = new RuleDefinition("trueAction", "true", "匹配");
        RuleDefinition falseAction = new RuleDefinition("falseAction", "false", "不匹配");

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
                                        action("!103&&104", action("trueAction"), action("falseAction")
                                        )
                                ))
                        )
                ));
        RuleSuite suite = simpleRuleLoader.get();
        System.out.println(suite.checkScenario("demo", root));
    }


    @SneakyThrows
    @Test
    public void ruleTest2() {
        RuleEngine ruleEngine = new RuleEngine();

        RuleDefinition f1 = new RuleDefinition("1", "1==1", "");
        RuleDefinition f2 = new RuleDefinition("2", "2==2", "");
        RuleDefinition f3 = new RuleDefinition("3", "3!=3", "");
        RuleDefinition f4 = new RuleDefinition("4", "4!=4", "");
        RuleDefinition f5 = new RuleDefinition("5", "5==5", "");
        RuleDefinition f6 = new RuleDefinition("6", "6==6", "");
        //1：false；2：true；3：false；4：false；5：true，6：true；
        ruleEngine.register(f1);
        ruleEngine.register(f2);
        ruleEngine.register(f3);
        ruleEngine.register(f4);
        ruleEngine.register(f5);
        ruleEngine.register(f6);
        RuleChecker ruleChecker = new RuleChecker(ruleEngine);
//        String statement = "(!1||2)&&(3||!(4||(5&&6)))";
        String statement = "!3";
        User context = new User("jack", 19);
        ActionResult check = ruleChecker.check(statement, context);
        System.out.println(check);
    }


    @Data
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
    }

}
