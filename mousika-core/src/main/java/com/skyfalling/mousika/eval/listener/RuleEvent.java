package com.skyfalling.mousika.eval.listener;

import lombok.*;

/**
 * Created on 2022/2/15
 *
 * @author liyifei
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class RuleEvent {

    @NonNull
    private EventType eventType;

    @NonNull
    private String ruleExpr;

    private Object data;


    /**
     * 枚举类型
     */
    public enum EventType {
        PARSE_SUCCEED,
        PARSE_FAIL,
        EVAL_SUCCEED,
        EVAL_FAIL,
    }
}
