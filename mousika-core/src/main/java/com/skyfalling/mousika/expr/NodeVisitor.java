package com.skyfalling.mousika.expr;


import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.result.EvalResult;
import com.skyfalling.mousika.expr.DefaultNodeVisitor.EvalNode;

import java.util.List;

/**
 * 规则节点访问接口
 * Created on 2022/2/11
 *
 * @author liyifei
 */
public interface NodeVisitor {

    /**
     * 访问规则节点
     */
    EvalResult visit(RuleNode node);


    /**
     * 获取执行过的规则
     *
     * @return
     */
    List<EvalNode> getEvalRules();

    /**
     * 执行结束
     */
    void finish();

}