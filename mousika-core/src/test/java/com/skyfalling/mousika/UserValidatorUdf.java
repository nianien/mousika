package com.skyfalling.mousika;


import com.skyfalling.mousika.define.Functions.Function3;
import com.skyfalling.mousika.define.Udf;
import com.skyfalling.mousika.eval.RuleContext;
import lombok.NoArgsConstructor;

@Udf
@NoArgsConstructor
public class UserValidatorUdf implements Function3<String, Integer, RuleContext, Boolean> {

    @Override
    public Boolean apply(String name, Integer age, RuleContext context) {
        context.setProperty("agentId", "代理商A");
        context.setProperty("customerId", "客户B");
        boolean success = name != null && age > 18;
        System.out.println("==>UserValidatorUdf:" + success);
        return success;
    }


}
