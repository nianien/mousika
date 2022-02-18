package com.skyfalling.mousika.eval.node;

import com.skyfalling.mousika.eval.RuleContext;

/**
 * Created on 2022/2/18
 *
 * @author liyifei
 */
public enum NopNode implements RuleNode {

    SINGLETON;

    @Override
    public Object eval(RuleContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "nop";
    }
}
