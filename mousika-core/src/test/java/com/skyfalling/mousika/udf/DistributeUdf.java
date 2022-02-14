package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.define.Udf;
import lombok.NoArgsConstructor;

import java.util.function.BiFunction;

@Udf
@NoArgsConstructor
public class DistributeUdf implements BiFunction<String, String, Boolean> {

    @Override
    public Boolean apply(String user, String owner) {
        System.out.println("账号: " + user + "责任人分配给 " + owner + "!");
        return true;
    }

}
