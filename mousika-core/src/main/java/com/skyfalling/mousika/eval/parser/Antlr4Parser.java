package com.skyfalling.mousika.eval.parser;

import com.nianien.antlr4.RuleLexer;
import com.nianien.antlr4.RuleParser;
import com.nianien.antlr4.RuleVisitor;
import com.skyfalling.mousika.eval.node.RuleNode;
import org.antlr.v4.runtime.*;

/**
 * 基于Antlr4的规则解析
 */
public class Antlr4Parser {

    public static RuleNode parse(String expression, NodeGenerator generator) {
        RuleLexer lexer = new RuleLexer(new ANTLRInputStream(expression));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg,
                                    RecognitionException e) {
                throw new IllegalStateException(msg, e);
            }
        });
        RuleParser parser = new RuleParser(new CommonTokenStream(lexer));
        RuleVisitor visitor = new DefaultRuleVisitor(generator);
        return (RuleNode) visitor.visit(parser.expr());
    }
}
