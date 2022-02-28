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
     * 影响评估
     */
    private List<List<RuleResult>> details;


    @Override
    public String toString() {
        return "ActionResult{" +
                "result=" + result +
                ", details=" + details +
                '}';
    }
}