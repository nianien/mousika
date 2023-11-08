package com.skyfalling.mousika.engine;

import com.cudrania.core.utils.StringUtils;
import com.skyfalling.mousika.udf.Functions;
import com.skyfalling.mousika.udf.UdfDelegate;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author : liyifei
 * @created : 2023/11/7, 星期二
 * Copyright (c) 2004-2029 All Rights Reserved.
 **/
public class UdfContainer {
    /**
     * 注册的UDF
     */
    private Map<String, Object> udfDefined = new ConcurrentHashMap<>();

    /**
     * 编译后的UDF
     */
    private Map<String, Object> udfCompiled = new ConcurrentHashMap<>();


    /**
     * 获取编译后的UDF
     *
     * @return
     */
    public Map<String, Object> compileUdf() {
        if (this.udfCompiled == null) {
            synchronized (this) {
                if (this.udfCompiled == null) {
                    Map<String, Object> udfCompiled = new ConcurrentHashMap<>();
                    //编译udf
                    for (Entry<String, Object> entry : udfDefined.entrySet()) {
                        udfCompiled.put(entry.getKey(), compileUdf("UDF$" + StringUtils.capitalize(entry.getKey()), entry.getValue()));
                    }
                    this.udfCompiled = udfCompiled;
                }
            }
        }
        return udfCompiled;
    }


    /**
     * 注册自定义函数
     *
     * @param udfDefinition
     */
    public void register(UdfDefinition udfDefinition) {
        register(udfDefinition.getGroup(), udfDefinition.getName(), udfDefinition.getUdf());
        this.udfCompiled = null;
    }

    /**
     * 注册自定义函数
     *
     * @param group
     * @param name
     * @param udf
     */
    protected void register(String group, String name, Object udf) {
        Map map = this.udfDefined;
        if (StringUtils.isNotEmpty(group)) {
            String[] tokens = group.replaceAll("\\s", "").split("\\.+");
            int i = 0;
            for (; i < tokens.length; i++) {
                String token = tokens[i];
                Object o = map.computeIfAbsent(token, k -> new HashMap<>());
                if (o instanceof Map) {
                    map = (Map) o;
                } else {
                    String conflictName = Arrays.stream(tokens).limit(i + 1).collect(Collectors.joining("."));
                    throw new IllegalArgumentException("udf: " + conflictName + " is already defined!");
                }
            }
        }
        if (map.containsKey(name)) {
            throw new IllegalArgumentException("udf: " + name + " is already defined!");
        }
        doRegister(map, name, udf);
    }

    /**
     * 注册自定义函数
     *
     * @param map
     * @param name
     * @param udf
     */
    protected void doRegister(Map map, String name, Object udf) {
        Class<?>[] interfaces = udf.getClass().getInterfaces();
        for (Class<?> anInterface : interfaces) {
            //只代理通过Functions定义的udf
            if (anInterface.getName().indexOf(Functions.class.getName()) != -1) {
                map.put(name, UdfDelegate.of(udf));
                return;
            }
        }
        map.put(name, udf);
    }


    /**
     * 将udf映射表编译成类对象<p/>
     *
     * @param name
     * @param udf
     * @return
     */
    @SneakyThrows
    private static Object compileUdf(String name, Object udf) {
        if (!(udf instanceof Map)) {
            return udf;
        }
        Map<String, Object> udfMap = (Map<String, Object>) udf;
        Builder<Object> subclass = new ByteBuddy()
                .subclass(Object.class)
                .name(name);
        for (Entry<String, Object> entry : udfMap.entrySet()) {
            subclass = subclass.defineField(entry.getKey(), Object.class, Visibility.PUBLIC);
        }
        Object instance = subclass
                .make()
                .load(Thread.currentThread().getContextClassLoader())
                .getLoaded().newInstance();
        for (Entry<String, Object> entry : udfMap.entrySet()) {
            instance.getClass().getField(entry.getKey()).set(instance, compileUdf(name + "$" + StringUtils.capitalize(entry.getKey()), entry.getValue()));
        }
        return instance;
    }
}
