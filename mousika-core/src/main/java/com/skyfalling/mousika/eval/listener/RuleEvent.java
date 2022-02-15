package com.skyfalling.mousika.eval.listener;

import com.skyfalling.mousika.eval.json.JsonUtils;
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


    @SneakyThrows
    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }


    /**
     * 枚举类型
     */
    public enum EventType {
        PARSE, EVAL
    }
}
