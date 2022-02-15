package com.skyfalling.mousika.eval.listener;

/**
 * Created on 2022/2/15
 *
 * @author liyifei
 */
public interface RuleListener {


    /**
     * 规则解析事件
     *
     * @param event
     */
    void onParse(RuleEvent event);

    /**
     * 规则评估事件
     *
     * @param event
     */
    void onEval(RuleEvent event);
}
