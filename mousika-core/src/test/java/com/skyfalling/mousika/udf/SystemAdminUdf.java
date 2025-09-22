package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.annotation.Udf;
import com.skyfalling.mousika.eval.context.UdfContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
@Udf(group = "policy")
@NoArgsConstructor
@AllArgsConstructor
public class SystemAdminUdf implements BiFunction<String, UdfContext, Boolean> {

    private String admin;

    @Override
    public Boolean apply(String name, UdfContext ruleContext) {
        System.out.println("@@@@" + ruleContext.getRule());
        ruleContext.setProperty("admin", admin);
        return Objects.equals(name, admin);
    }

}
