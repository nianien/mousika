package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.define.Udf;
import com.skyfalling.mousika.eval.RuleContext;
import lombok.NoArgsConstructor;

import java.util.function.BiFunction;

@Udf
@NoArgsConstructor
public class FindAgentTypeUdf implements BiFunction<String, RuleContext, Integer> {


    @Override
    public Integer apply(String user, RuleContext context) {
        int type = findAgentType(user, context);
        context.setProperty("agent_type", type);
        return type;
    }


    private int findAgentType(String user, RuleContext context) {
        if (user.contains("a")) {
            context.setProperty("owner", "owner16");
            return 16;
        } else {
            context.setProperty("owner", "owner21");
            return 21;
        }
    }

}
