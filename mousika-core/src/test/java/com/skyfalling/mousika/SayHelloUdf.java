package com.skyfalling.mousika;

import com.skyfalling.mousika.define.Udf;
import com.skyfalling.mousika.eval.RuleContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.function.BiFunction;

@Udf
@NoArgsConstructor
@AllArgsConstructor
public class SayHelloUdf implements BiFunction<String, RuleContext, Boolean> {

    private String name;


    @Override
    public Boolean apply(String s, RuleContext context) {
        System.out.println(name + " saying hello to " + s + "!");
        context.setProperty("name", name);
        return false;
    }

}
