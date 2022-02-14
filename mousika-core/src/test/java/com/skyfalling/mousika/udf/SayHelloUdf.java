package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.define.Udf;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;

@Udf
@NoArgsConstructor
public class SayHelloUdf implements Consumer<String> {

    @Override
    public void accept(String user) {
        System.out.println("say hello to: " + user + "!");
    }

}
