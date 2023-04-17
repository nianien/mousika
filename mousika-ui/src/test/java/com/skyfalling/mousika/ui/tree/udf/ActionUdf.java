package com.skyfalling.mousika.ui.tree.udf;

import com.skyfalling.mousika.annotation.Udf;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;

/**
 * Created on 2022/7/1
 *
 * @author liyifei
 */
@Udf
@NoArgsConstructor
public class ActionUdf implements Consumer<String> {
    @Override
    public void accept(String s) {
        System.out.println("执行操作:" + s);
    }
}
