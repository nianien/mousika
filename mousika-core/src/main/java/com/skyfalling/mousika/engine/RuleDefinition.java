package com.skyfalling.mousika.engine;

import lombok.*;

/**
 * 规则定义
 * @author liyifei 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class RuleDefinition {

    /**
     * 规则id
     */
    @NonNull
    private String ruleId;
    /**
     * 规则表达式
     */
    @NonNull
    private String expression;
    /**
     * 规则描述
     */
    private String desc;


}
