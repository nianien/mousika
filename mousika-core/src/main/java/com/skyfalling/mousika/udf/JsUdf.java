package com.skyfalling.mousika.udf;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author : liyifei
 * @created : 2023/12/8, 星期五
 * Copyright (c) 2004-2029 All Rights Reserved.
 **/
public class JsUdf implements Function<Object, Object> {

    private ThreadLocal<Function<Object[], Object>> jsFunctionCompiled;

    /**
     * JS函数UDF
     *
     * @param name       函数名称
     * @param jsFunction 函数定义
     * @param compiler   函数编译器
     */
    public JsUdf(String name, String jsFunction, BiFunction<String, String, Object> compiler) {
        jsFunctionCompiled = ThreadLocal.withInitial(() -> {
            Function<Object[], Object> value = (Function<Object[], Object>) compiler.apply(name, jsFunction);
            return value;
        });
    }

    @Override
    public Object apply(Object objects) {
        return jsFunctionCompiled.get().apply(new Object[]{objects});
    }


}
