package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.define.Udf;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;

@Udf
@NoArgsConstructor
public class ActionUdf implements Function<String,Boolean> {

    @Override
    public Boolean apply(String s) {
        System.out.println("报备 " + s + "!");
        return true;
    }

}
