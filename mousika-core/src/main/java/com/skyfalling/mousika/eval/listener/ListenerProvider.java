package com.skyfalling.mousika.eval.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听器驱动
 * Created on 2022/2/15
 *
 * @author liyifei
 */
public class ListenerProvider implements RuleListener {

    /**
     * 默认单例
     */
    public static final ListenerProvider DEFAULT = new ListenerProvider();

    /**
     * 注册的监听器列表
     */
    private List<RuleListener> listeners = new ArrayList<>();


    @Override
    public void onParse(RuleEvent event) {
        for (RuleListener listener : listeners) {
            listener.onParse(event);
        }
    }

    @Override
    public void onEval(RuleEvent event) {
        for (RuleListener listener : listeners) {
            listener.onEval(event);
        }
    }


    /**
     * 注册监听器
     */
    public void register(RuleListener listener) {
        this.listeners.add(listener);
    }

}