package com.skyfalling.mousika.udf;

import lombok.SneakyThrows;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.function.Function;

/**
 * @author : liyifei
 * @created : 2023/12/8, 星期五
 * Copyright (c) 2004-2029 All Rights Reserved.
 **/
public class JsUdf implements Function<Object[], Object> {


    private final String funcName;
    private final String funcBody;


    /**
     * 类级线程副本
     */
    private static ThreadLocal<ScriptEngine> engineFactory = ThreadLocal.withInitial(() -> new ScriptEngineManager().getEngineByName("js"));
    /**
     * 实例级线程副本
     */
    private ThreadLocal<Object> funcFactory = ThreadLocal.withInitial(this::createFuncObject);


    /**
     * JS函数UDF
     *
     * @param funcName 函数名称
     * @param funcBody 函数定义
     */
    @SneakyThrows
    public JsUdf(String funcName, String funcBody) {
        this.funcName = funcName;
        this.funcBody = funcBody;
    }

    @Override
    public Object apply(Object... objects) {
        Function<Object[], Object> func = (Function<Object[], Object>) funcFactory.get();
        return func.apply(objects);
    }

    @SneakyThrows
    private Object createFuncObject() {
        ScriptEngine engine = engineFactory.get();
        engine.eval(funcBody);
        return engine.getBindings(ScriptContext.ENGINE_SCOPE).get(funcName);
    }

}
