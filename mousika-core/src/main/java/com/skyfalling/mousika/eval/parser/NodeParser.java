package com.skyfalling.mousika.eval.parser;


import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.node.Node;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import static com.skyfalling.mousika.eval.ActionBuilder.build;
import static com.skyfalling.mousika.eval.parser.Operator.*;

/**
 * 规则解析器，用于生成规则节点树
 *
 * @author liyifei
 */
public class NodeParser {
    private static final int TOK_WORD = 2;
    private static final int TOK_OP = 4;
    private static final int TOK_OPEN = 8;
    private static final int TOK_CLOSE = 16;


    /**
     * @param expression 规则集表达式,形式为规则ID的逻辑组合,如(1||2)&&(3||!4)
     * @param generator
     * @return
     */
    public static Node parse(String expression, Function<String, Node> generator) {
        return doParse(tokenize(expression), generator);
    }



    /**
     * 解析表达式
     *
     * @param input
     * @return
     */
    private static List<String> tokenize(String input) {
        int pos = 0;
        int expected = TOK_OPEN | TOK_WORD;
        List<String> tokens = new ArrayList<>();
        while (pos < input.length()) {
            String tok = "";
            char c = input.charAt(pos);
            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }
            if (!isOpChar(c)) {
                if ((expected & TOK_WORD) == 0) {
                    throw new IllegalArgumentException("Unexpected identifier: " + (tok + c));
                }
                expected = TOK_OP | TOK_OPEN | TOK_CLOSE;
                while (!isOpChar(c) && pos < input.length()) {
                    tok = tok + input.charAt(pos);
                    pos++;
                    if (pos < input.length()) {
                        c = input.charAt(pos);
                    } else {
                        c = 0;
                    }
                }
            } else if (c == '(' || c == ')') {
                tok = tok + c;
                pos++;
                if (c == '(' && (expected & TOK_OPEN) != 0) {
                    expected = TOK_WORD | TOK_OPEN | TOK_CLOSE;
                } else if (c == ')' && (expected & TOK_CLOSE) != 0) {
                    expected = TOK_OP | TOK_CLOSE;
                } else {
                    throw new IllegalArgumentException("Parens mismatched:" + tok);
                }
            } else {
                if ((expected & TOK_OP) == 0) {
                    if (c != '-' && c != '!' && c != '~') {
                        throw new IllegalArgumentException("Missing operand:" + (tok + c));
                    }
                    tok = tok + c;
                    pos++;
                } else {
                    String lastOp = null;
                    while (isOpChar(c) && !Character.isWhitespace(c) && c != '(' && c != ')' && pos < input.length()) {
                        if (of(tok + input.charAt(pos)) != null) {
                            tok = tok + input.charAt(pos);
                            lastOp = tok;
                        } else if (lastOp == null) {
                            tok = tok + input.charAt(pos);
                        } else {
                            break;
                        }
                        pos++;
                        if (pos < input.length()) {
                            c = input.charAt(pos);
                        } else {
                            c = 0;
                        }
                    }
                    if (lastOp == null) {
                        throw new IllegalArgumentException("Bad operator:" + (tok + c));
                    }
                }
                expected = TOK_WORD | TOK_OPEN;
            }
            tokens.add(tok);
        }
        return tokens;
    }


    /**
     * 生成规则树
     *
     * @param tokens
     * @param generator 节点生成器
     * @return
     */
    private static Node doParse(List<String> tokens, Function<String, Node> generator) {
        // 变量栈
        Stack<Node> es = new Stack<>();
        // 操作符栈
        Stack<Operator> os = new Stack<>();
        // 扫描结果
        for (String token : tokens) {
            Operator right = of(token);
            if (right != null) {
                Operator left = os.isEmpty() ? null : os.peek();
                //ordinal越小优先级越高
                while (left != null && isPrecede(right, left) < 0) {
                    es.push(doOperate(left, es, os));
                    os.pop();
                    left = os.isEmpty() ? null : os.peek();
                }
                //没有匹配的括号
                if (left == null && right == Operator.PAREN_CLOSE) {
                    throw new IllegalArgumentException("Unmatched parenthesis");
                }
                //匹配括号
                if (left != null && isPrecede(right, left) == 0) {
                    os.pop();
                    continue;
                }
                //其他情况，操作符入栈
                if (left == null || isPrecede(right, left) > 0) {
                    os.push(right);
                } else {
                    os.pop();
                }
            } else {
                es.push(generator.apply(token));
            }
        }
        while (!os.isEmpty()) {
            Operator op = os.pop();
            if (op == Operator.PAREN_OPEN || op == Operator.PAREN_CLOSE) {
                return null; // Bad paren
            }
            Node e = doOperate(op, es, os);
            if (e == null) {
                return null;
            }
            es.push(e);
        }
        if (es.isEmpty()) {
            return null;
        } else {
            return es.pop();
        }
    }

    /**
     * 节点运算
     *
     * @param op
     * @param es
     * @param os
     * @return
     */
    private static Node doOperate(Operator op, Stack<Node> es, Stack<Operator> os) {
        try {
            Node b = es.pop();
            Node a2 = null;
            Node a1 = null;
            if (op.getArgNums() > 1) {
                a2 = es.pop();
            }
            if (op.getArgNums() > 2) {
                a1 = es.pop();
            }
            switch (op) {
                case LOGICAL_AND:
                    return ((RuleNode) a2).and((RuleNode) b);
                case LOGICAL_OR:
                    return ((RuleNode) a2).or((RuleNode) b);
                case UNARY_LOGICAL_NOT:
                    return ((RuleNode) b).not();
                case COLON:
                    os.pop();
                    return build(a1, a2, b);
                default:
                    //Unsupported Operator
                    throw new UnsupportedOperationException("Unsupported operator:" + op);
            }
        } catch (EmptyStackException e) {
            throw new IllegalArgumentException("Missing operand:" + op);
        }
    }

}
