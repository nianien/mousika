package com.skyfalling.mousika.eval;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionResult {
    /**
     * 执行结果
     */
    private Object result;
    /**
     * 匹配详情
     */
    private List<RuleResult> details;
    /**
     * 是否有结果
     */
    private boolean hasResult = true;

    public static ActionResult NO_RESULT = new ActionResult(false);

    public ActionResult(Object result, List<RuleResult> details) {
        this(result, details, true);
    }


    public ActionResult(boolean hasResult) {
        this(null, null, hasResult);
    }

    @Override
    public String toString() {
        return "ActionResult{" +
                "result=" + result +
                ", details=" + details +
                '}';
    }
}