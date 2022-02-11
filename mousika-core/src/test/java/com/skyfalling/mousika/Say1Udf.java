package com.skyfalling.mousika;


import com.skyfalling.mousika.define.Udf;

import java.util.function.Function;

/**
 * @author liujiakun03 <liujiakun03@kuaishou.com>
 * Created on 2021-11-10
 */
@Udf
public class Say1Udf implements Function<Integer, Boolean> {

    @Override
    public Boolean apply(Integer s) {
        return s % 2 == 0;
    }
}
