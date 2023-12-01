package com.skyfalling.mousika.udf;

import com.cudrania.core.reflection.Reflections;
import com.fasterxml.jackson.core.type.TypeReference;
import com.skyfalling.mousika.utils.JsonUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Udf代理接口, 默认实现类型自动转化
 *
 * @param <P>
 * @param <R>
 */
@FunctionalInterface
public interface UdfDelegate<P, R> {


    /**
     * 参数列表
     *
     * @param ps
     * @return
     */
    R apply(P... ps);


    /**
     * 创建udf代理
     *
     * @param udf
     * @return
     */
    static UdfDelegate of(Object udf) {
        return new Wrapper(udf);
    }

    /**
     * UDF代理类
     * Created on 2022/12/6
     *
     * @author liyifei
     */

    class Wrapper implements UdfDelegate {


        /**
         * 代理函数
         */
        private Object udf;

        /**
         * @param udf 代理函数
         */
        public Wrapper(Object udf) {
            this.udf = udf;
        }


        /**
         * @param params
         * @return
         */
        @Override
        public Object apply(Object... params) {
            int n = params.length;
            Optional<Method> found = Reflections.getMethods(udf.getClass(), m -> m.getName().equals("apply") && m.getParameterCount() == n && !m.isBridge() //the overloaded method is marked as being a "bridge method".
            ).stream().findFirst();
            if (!found.isPresent()) {
                throw new RuntimeException("no compatible method has " + params.length + " parameters!");
            }
            Method method = found.get();
            Object[] casts = convert(params, method.getGenericParameterTypes());
            return Reflections.invoke(method, udf, casts);
        }


        public static Object[] convert(Object[] parameters, Type[] parameterTypes) {
            Object[] castParams = new Object[parameters.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Type parameterType = parameterTypes[i];
                castParams[i] = convert(parameters[i], parameterType);
            }
            return castParams;
        }


        private static Object convert(Object parameter, Type parameterType) {
            if (parameterType instanceof Class) {
                Class clazz = (Class) parameterType;
                if (clazz.isInstance(parameter)) {
                    return parameter;
                }
                if (parameter instanceof String) {
                    String valueString = (String) parameter;
                    if (clazz.equals(String.class)) {
                        return valueString;
                    }
                    if (clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class)) {
                        return Boolean.valueOf(valueString);
                    }
                    if (clazz.equals(Byte.TYPE) || clazz.equals(Byte.class)) {
                        return Byte.valueOf(valueString);
                    }
                    if (clazz.equals(Short.TYPE) || clazz.equals(Short.class)) {
                        return Short.valueOf(valueString);
                    }
                    if (clazz.equals(Integer.TYPE) || clazz.equals(Integer.class)) {
                        return Integer.valueOf(valueString);
                    }
                    if (clazz.equals(Long.TYPE) || clazz.equals(Long.class)) {
                        return Long.valueOf(valueString);
                    }
                    if (clazz.equals(Float.TYPE) || clazz.equals(Float.class)) {
                        return Float.valueOf(valueString);
                    }
                    if (clazz.equals(Double.TYPE) || clazz.equals(Double.class)) {
                        return Double.valueOf(valueString);
                    }
                    if (clazz.equals(Character.TYPE) || clazz.equals(Character.class)) {
                        return Character.valueOf(valueString.charAt(0));
                    }
                    if (clazz.isEnum()) {
                        return Enum.valueOf((Class<Enum>) clazz, valueString);
                    }
                }
            }
            return JsonUtils.toBean(JsonUtils.toJson(parameter), new TypeReference<>() {
                @Override
                public Type getType() {
                    return parameterType;
                }
            });
        }

    }
}