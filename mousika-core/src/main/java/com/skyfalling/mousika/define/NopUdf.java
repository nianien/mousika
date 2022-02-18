package com.skyfalling.mousika.define;

import com.skyfalling.mousika.eval.ActionResult;
import com.skyfalling.mousika.eval.NaResult;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@Udf
@NoArgsConstructor
public class NopUdf implements Supplier<ActionResult> {


    @Override
    public ActionResult get() {
        return NaResult.DEFAULT;
    }
}
