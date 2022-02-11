package com.skyfalling.mousika;

import com.skyfalling.mousika.engine.RuleEngine;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2022/2/11
 *
 * @author liyifei
 */
public class RuleEngineTest {


    @Test
    public void testDesc() {
        RuleEngine ruleEngine = new RuleEngine();
        String desc = "代理商【{$.agentId}】不允许【{$.customerId}】跨开{}";
        desc = "\"" + desc.replaceAll("\\{(\\$+\\..+?)\\}", "\\\"+$1+\\\"") + "\"";
        System.out.println(desc);
        Map<String, String> map = new HashMap<>();
        map.put("agentId", "a");
        map.put("customerId", "b");
        System.out.println(ruleEngine.eval(desc, map,null));
    }
}
