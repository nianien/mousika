package com.skyfalling.mousika;

import com.skyfalling.mousika.eval.ActionBuilder;
import com.skyfalling.mousika.eval.GeneratorFactory;
import com.skyfalling.mousika.eval.node.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2022/6/17
 *
 * @author liyifei
 */
public class Gen {


    static Map<String, String> rules = new HashMap<>();

    public static void main(String[] args) {

        rules.put("a", "1||b||c");
        rules.put("b", "2||d");
        rules.put("c", "3||5");
        rules.put("d", "4||c");
        rules.put("1001", "2+3>1");
        rules.put("1002", "5+3<3");
        rules.put("1003", "1001||1002");



        ActionBuilder.setGenerator(GeneratorFactory.create(rules));
        Node node = ActionBuilder.build("1001&&1002&&1003");
        System.out.println(node.expr());
    }


}
