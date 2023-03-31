package com.skyfalling.mousika.eval.result;

/**
 * 表示无结果,用于区别null
 */
public class NaResult extends NodeResult {

    public static final NaResult DEFAULT = new NaResult();

    private NaResult() {
    }

    @Override
    public String toString() {
        return "NaResult";
    }
}