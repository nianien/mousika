package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.annotation.Udf;
import com.skyfalling.mousika.eval.RuleContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.function.BiFunction;


@Udf(group = "policy")
@NoArgsConstructor
@AllArgsConstructor
public class SystemAdminUdf implements BiFunction<String, RuleContext, Boolean> {

    private String admin;

    @Override
    public Boolean apply(String name, RuleContext ruleContext) {
        System.out.println("@@@@" + ruleContext.getRule());
        ruleContext.setProperty("admin", admin);
        return Objects.equals(name, admin);
    }

}
