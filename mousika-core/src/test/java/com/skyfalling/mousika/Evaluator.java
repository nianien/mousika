package com.skyfalling.mousika;

import com.skyfalling.mousika.MainTest.RootObject;
import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.RuleEngine;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.RuleContext;
import com.skyfalling.mousika.eval.RuleContextImpl;
import com.skyfalling.mousika.eval.action.RuleAction;

import java.util.Arrays;
import java.util.List;

import static com.skyfalling.mousika.eval.action.RuleAction.action;

/**
 * Created on 2021/12/14
 *
 * @author liyifei
 */
public class Evaluator {
    public static void main(String[] args) {
        RuleEngine ruleEngine = new RuleEngine();
        List<RuleDefinition> ruleDefinitions = Arrays.asList(
                new RuleDefinition("1", "1==1", ""),
                new RuleDefinition("2", "2!=2", ""),
                new RuleDefinition("3", "3!=3", ""),
                new RuleDefinition("4", "4!=4", ""),
                new RuleDefinition("5", "baobei('a')", "报备A"),
                new RuleDefinition("6", "baobei('b');baobei('c')", "报备B和C"),
                new RuleDefinition("7", "'c'", "")
        );
        for (RuleDefinition ruleDefinition : ruleDefinitions) {
            ruleEngine.register(ruleDefinition);
        }
        UdfDefinition baobei = new UdfDefinition("baobei", new DcUdf());
        ruleEngine.register(baobei);
        RootObject root = new RootObject("jack", 19);

        RuleContext context = new RuleContextImpl(ruleEngine, root);
        RuleAction ruleAction = action("1&&2",
                action("5&&6"),
                action("!3&&4", action("6"), action("6&&7"))
        );
        System.out.println(ruleAction.execute(context));
    }


}
