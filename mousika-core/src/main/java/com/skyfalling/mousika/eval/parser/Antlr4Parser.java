package com.skyfalling.mousika.eval.parser;

import com.nianien.antlr4.RuleLexer;
import com.nianien.antlr4.RuleParser;
import com.nianien.antlr4.RuleVisitor;
import com.skyfalling.mousika.eval.node.RuleNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class Antlr4Parser {

    public static RuleNode parse(String expression, NodeGenerator generator) {
        RuleLexer lexer = new RuleLexer(new ANTLRInputStream(expression));
        RuleParser parser = new RuleParser(new CommonTokenStream(lexer));
        RuleVisitor visitor = new DefaultRuleVisitor(generator);
        return (RuleNode) visitor.visit(parser.expr());
    }
}
