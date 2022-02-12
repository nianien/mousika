package com.skyfalling.mousika;

import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.ActionResult;
import com.skyfalling.mousika.eval.RuleChecker;
import com.skyfalling.mousika.suite.RuleScenario;
import com.skyfalling.mousika.suite.RuleSuite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.skyfalling.mousika.eval.action.RuleAction.action;

public class MainTest {


    @SneakyThrows
    @Test
    public void ruleInTest() {
        //注意js中in的操作逻辑为以下这种
        RuleEngine ruleEngine = new RuleEngine();
        String ruleStr = "[5,6].indexOf($.q)!=-1";
        Map<String, Integer> map = new HashMap<>();
        map.put("q", 5);
        System.out.println(ruleEngine.eval(ruleStr, map, null));

        String ruleStr2 = "$.q != true";
        Map<String, Boolean> map2 = new HashMap<>();
        map2.put("q", true);
        System.out.println(ruleEngine.eval(ruleStr2, map2, null));
    }


    @SneakyThrows
    @Test
    public void ruleTest() {
        RuleDefinition c1 = new RuleDefinition("c1", "1!=1", "分支1");
        RuleDefinition c2 = new RuleDefinition("c2", "2==2", "分支2");

        RuleDefinition f1 = new RuleDefinition("101", "isNameAndAgeValid($.name,$.age,$$);", "代理商:【{$$.agentId}】,客户:【{$$.customerId}】");
        RuleDefinition f2 = new RuleDefinition("102", "sayHello($.name,$$)", "name:【{$.name}】不等于【{$$.name}】");
        RuleDefinition f3 = new RuleDefinition("103", "$.name!='' && $.age>18", "");
        RuleDefinition f4 = new RuleDefinition("104",
                "var udf= Java.type('com.skyfalling.mousika.SayHelloUdf'); new udf('js').apply($.name,$$)", "");

        RuleDefinition fTrue = new RuleDefinition("ft", "true", "匹配");
        RuleDefinition fFalse = new RuleDefinition("ff", "false", "不匹配");

        UdfDefinition udf1 = new UdfDefinition("sayHello", new SayHelloUdf("system"));
        UdfDefinition udf2 = new UdfDefinition("isNameAndAgeValid", new UserValidatorUdf());
        RootObject root = new RootObject("jack", 17);

        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(c1, c2, f1, f2, f3, f4, fTrue, fFalse),
                Arrays.asList(udf1, udf2),
                Arrays.asList(new RuleScenario("jinniu", Arrays.asList(
                                action("c1",
                                        action("!101&&!102", action("ft"), action("ff"))/*,
                                        new RuleAction("ft")*/
                                ),
                                action("c2",
                                        action("!103&&!104", action("ft"), action("ff")
                                        )
                                ))
                        )
                ));
        RuleSuite suite = simpleRuleLoader.get();
        System.out.println(suite.checkScenario("jinniu", root));
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
        RootObject context = new RootObject("jack", 19);
        ActionResult check = ruleChecker.check(statement, context);
        System.out.println(check);
    }


    @Data
    @AllArgsConstructor
    public static class RootObject {
        String name;
        int age;
    }

}
