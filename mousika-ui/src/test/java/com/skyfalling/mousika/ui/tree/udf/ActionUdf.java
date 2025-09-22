package com.skyfalling.mousika.ui.tree.udf;

import com.skyfalling.mousika.annotation.Udf;
import lombok.NoArgsConstructor;

import java.util.function.Function;

/**
 * Created on 2022/7/1
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
@Udf
@NoArgsConstructor
public class ActionUdf implements Function<String, String> {
    @Override
    public String apply(String s) {
        System.out.println("执行操作:" + s);
        return s;
    }
}
