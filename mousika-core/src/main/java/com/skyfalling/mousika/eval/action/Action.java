package com.skyfalling.mousika.eval.action;

import com.skyfalling.mousika.eval.ActionResult;
import com.skyfalling.mousika.eval.RuleContext;

/**
 * 定义规则执行动作
 * Created on 2022/2/10
 *
 * @author liyifei
 */
public interface Action {
    /**
     * 执行规则定义的动作
     *
     * @param context
     * @return
     */
    ActionResult execute(RuleContext context);

}
