package com.skyfalling.mousika.udf;

import com.skyfalling.mousika.annotation.Udf;
import com.skyfalling.mousika.udf.Functions.Function3;
import com.skyfalling.mousika.udf.Functions.Function4;

import java.util.List;
import java.util.Map;

/**
 * 嵌套UDF测试
 *
 * @author liujiakun03 <liujiakun03@kuaishou.com>
 * Created on 2023-02-02
 */
@Udf(group = "policy")
public class CallAnotherUdfUdf implements
        Function4<String, List<String>, Map<String, Object>, Function3<String, List<String>, Map<String, Object>,
                Object>, Object> {

    @Override
    public Object apply(String name, List<String> strings, Map<String, Object> stringObjectMap,
                        Function3<String, List<String>, Map<String, Object>, Object> function3) {
        System.out.println("udf invoked: " + function3.getClass() + ",args:" + name);
        return function3.apply(name, strings, stringObjectMap);
    }
}
