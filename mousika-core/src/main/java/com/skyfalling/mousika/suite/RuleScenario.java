package com.skyfalling.mousika.suite;

import com.skyfalling.mousika.eval.action.RuleAction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 规则场景
 *
 * @author liyifei <liyifei@kuaishou.com>
 */
@Data
@AllArgsConstructor
public class RuleScenario {

    /**
     * 场景ID
     */
    private String id;
    /**
     * 规则对列表
     */
    private List<RuleAction> ruleActions;


}
