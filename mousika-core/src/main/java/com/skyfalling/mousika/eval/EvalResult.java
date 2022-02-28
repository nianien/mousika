package com.skyfalling.mousika.eval;

import lombok.Data;

/**
 * 单规则评估结果
 */
@Data
public class EvalResult {
    /**
     * 引擎计算返回值
     */
    private final Object result;
    /**
     * 当作为判断条件时,返回值转boolean值
     */
    private final boolean matched;

    public EvalResult(Object result) {
        this.result = result;
        this.matched = parseBoolean(result);
    }


    /**
     * boolean值解析
     *
     * @param res
     * @return
     */
    private boolean parseBoolean(Object res) {
        if (res == null) {
            return false;
        }
        if (res instanceof Boolean) {
            return (Boolean) res;
        }
        if (res instanceof Number) {
            return ((Number) res).floatValue() > 0;
        }
        if (res instanceof String) {
            return ((String) res).toLowerCase().matches("yes|true|1");
        }
        return false;
    }
}