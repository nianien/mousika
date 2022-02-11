package com.skyfalling.mousika;


import com.skyfalling.mousika.define.Udf;

import java.util.Map;
import java.util.function.Function;

/**
 * @author liujiakun03 <liujiakun03@kuaishou.com>
 * Created on 2021-11-10
 */
@Udf
public class Say6Udf implements Function<Map<String, Object>, Boolean> {

    @Override
    public Boolean apply(Map<String, Object> map) {
        Object value = map.get("agent.agentId");
        return value.equals(5);
    }
}
