package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.annotation.Udf;
import com.skyfalling.mousika.eval.visitor.RuleVisitor;
import lombok.NoArgsConstructor;

import java.util.function.BiFunction;

@Udf
@NoArgsConstructor
public class GetUserTypeUdf implements BiFunction<String, RuleVisitor, Integer> {


    @Override
    public Integer apply(String name, RuleVisitor context) {
        int type = findUserType(name, context);
        context.setProperty("user_type", type);
        return type;
    }


    private int findUserType(String name, RuleVisitor context) {
        if (name.contains("admin")) {
            context.setProperty("owner", "admin");
            return 1;
        } else {
            context.setProperty("owner", "user");
            return 2;
        }
    }

}