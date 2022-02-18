package com.skyfalling.mousika.eval;

/**
 * 表示无结果,用于区别null
 */
public class NaResult extends ActionResult {

    public static final NaResult DEFAULT = new NaResult();

    private NaResult() {
    }
}