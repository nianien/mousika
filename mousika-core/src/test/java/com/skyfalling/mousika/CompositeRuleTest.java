package com.skyfalling.mousika;

import com.skyfalling.mousika.eval.ActionBuilder;
import com.skyfalling.mousika.eval.GeneratorFactory;
import com.skyfalling.mousika.eval.node.Node;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2022/6/17
 *
 * @author liyifei
 */
public class CompositeRuleTest {


    static Map<String, String> compositeRules = new HashMap<>();

    @Test
    public void test() {

        compositeRules.put("a", "1||b||c");
        compositeRules.put("b", "2||d");
        compositeRules.put("c", "3||5");
        compositeRules.put("d", "4||c");
        compositeRules.put("1003", "1001||1002");

        ActionBuilder.setGenerator(GeneratorFactory.create(compositeRules));
        Node node = ActionBuilder.build("1001&&1002&&1003");
        System.out.println(node.expr());
    }

}
