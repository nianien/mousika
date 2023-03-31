package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.annotation.Udf;
import com.skyfalling.mousika.udf.Functions.Function1;
import lombok.NoArgsConstructor;

@Udf(group = "policy")
@NoArgsConstructor
public class SayHelloUdf implements Function1<String, Object> {

    @Override
    public Object apply(String user) {
        System.out.println("say hello to: " + user + "!");
        return user;
    }

}
