package com.skyfalling.mousika.udf;

import com.cudrania.core.reflection.Reflections;
import com.skyfalling.mousika.utils.JsonUtils;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiFunction;

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

        private static BiFunction<Object, Class, Object> converter = (s, t) -> {
            s = convertIntoJavaObject(s);
            if (s instanceof Wrapper) {
                return ((Wrapper) s).udf; //嵌套udf的参数在嵌套方法中已处理
            }
            if (!(s instanceof String)) {
                s = JsonUtils.toJson(s);
            }
            Object v = JsonUtils.toBean((String) s, t);
            return v != null ? v : s;
        };

        // do ScriptObjectMirror JS -> Java conversion fix array converted map problem
        private static Object convertIntoJavaObject(Object scriptObj) {
            return scriptObj;
        }

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
            Optional<Method> found = Reflections.getMethods(udf.getClass(),
                            m -> m.getName().equals("apply")
                                    && m.getParameterCount() == n
                                    && !m.isBridge() //the overloaded method is marked as being a "bridge method".
                    )
                    .stream().findFirst();
            if (!found.isPresent()) {
                throw new RuntimeException("no compatible method has " + params.length + " parameters!");
            }
            Method method = found.get();
            Object[] casts = Reflections.convert(params, method.getParameterTypes(), converter);
            return Reflections.invoke(method, udf, casts);
        }


    }

}