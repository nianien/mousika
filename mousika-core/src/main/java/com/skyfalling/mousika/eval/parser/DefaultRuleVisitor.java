package com.skyfalling.mousika.eval.parser;

import com.nianien.antlr4.RuleBaseVisitor;
import com.nianien.antlr4.RuleParser;
import com.skyfalling.mousika.eval.node.CaseNode;
import com.skyfalling.mousika.eval.node.LimitNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认规则计算
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
@AllArgsConstructor
public class DefaultRuleVisitor extends RuleBaseVisitor {

    private NodeGenerator generator;

    @Override
    public Object visitPAR(RuleParser.PARContext ctx) {
        RuleNode r1 = (RuleNode) ctx.expr(0).accept(this);
        RuleNode r2 = (RuleNode) ctx.expr(1).accept(this);
        return r1.next(r2);
    }

    @Override
    public Object visitSER(RuleParser.SERContext ctx) {
        RuleNode r1 = (RuleNode) ctx.expr(0).accept(this);
        RuleNode r2 = (RuleNode) ctx.expr(1).accept(this);
        return r1.next(r2);
    }

    @Override
    public Object visitOR(RuleParser.ORContext ctx) {
        RuleNode r1 = (RuleNode) ctx.expr(0).accept(this);
        RuleNode r2 = (RuleNode) ctx.expr(1).accept(this);
        return r1.or(r2);
    }


    @Override
    public Object visitAND(RuleParser.ANDContext ctx) {
        RuleNode r1 = (RuleNode) ctx.expr(0).accept(this);
        RuleNode r2 = (RuleNode) ctx.expr(1).accept(this);
        return r1.and(r2);
    }

    @Override
    public Object visitIF(RuleParser.IFContext ctx) {
        RuleNode r1 = (RuleNode) ctx.expr(0).accept(this);
        RuleNode r2 = (RuleNode) ctx.expr(1).accept(this);
        RuleParser.ExprContext expr = ctx.expr(2);
        RuleNode r3 = expr == null ? null : (RuleNode) expr.accept(this);
        return new CaseNode(r1, r2, r3);
    }


    @Override
    public Object visitNOT(RuleParser.NOTContext ctx) {
        RuleNode r1 = (RuleNode) ctx.expr().accept(this);
        return r1.not();
    }


    @Override
    public Object visitLIMIT(RuleParser.LIMITContext ctx) {
        List<RuleNode> nodes = (List<RuleNode>) ctx.arguments().accept(this);
        int from = Integer.parseInt(ctx.CONST(0).getText().replaceAll("'", ""));
        int to = Integer.parseInt(ctx.CONST(1).getText().replaceAll("'", ""));
        return new LimitNode(from, to, nodes);
    }

    @Override
    public Object visitArguments(RuleParser.ArgumentsContext ctx) {
        return ctx.expr().stream()
                .map(e -> (RuleNode) e.accept(this))
                .collect(Collectors.toList());
    }


    @Override
    public Object visitID(RuleParser.IDContext ctx) {
        return generator.apply(ctx.getText());
    }

    @Override
    public Object visitPAREN(RuleParser.PARENContext ctx) {
        return ctx.expr().accept(this);
    }
}
