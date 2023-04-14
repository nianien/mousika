package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.eval.visitor.RuleContext;
import com.skyfalling.mousika.udf.Functions.Function3;

/**
 * Created on 2022/2/14
 *
 * @author liyifei
 */
public class AdultValidateUdf implements Function3<String, Integer, RuleContext, Boolean> {


    private int minAge;

    public AdultValidateUdf(int minAge) {
        this.minAge = minAge;
    }

    @Override
    public Boolean apply(String name, Integer age, RuleContext ruleContext) {
        System.out.println("@@@@current rule:" + ruleContext.getRule());
        ruleContext.setProperty("minAge", minAge);
        return age > minAge;
    }
}
