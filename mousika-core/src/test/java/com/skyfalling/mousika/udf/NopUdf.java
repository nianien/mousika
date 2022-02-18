package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.define.Udf;
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
