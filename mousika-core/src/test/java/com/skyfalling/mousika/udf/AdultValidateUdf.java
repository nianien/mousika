package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.eval.context.UdfContext;
import com.skyfalling.mousika.udf.Functions.Function3;

/**
 * Created on 2022/2/14
 *
 * @author liyifei
 */
public class AdultValidateUdf implements Function3<String, Integer, UdfContext, Boolean> {


    private int minAge;

    public AdultValidateUdf(int minAge) {
        this.minAge = minAge;
    }

    @Override
    public Boolean apply(String name, Integer age, UdfContext ruleContext) {
        System.out.println("@@@@current rule:" + ruleContext.getRule());
        ruleContext.setProperty("minAge", minAge);
        return age > minAge;
    }
}
