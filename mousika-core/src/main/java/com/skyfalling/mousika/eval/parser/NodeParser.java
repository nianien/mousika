package com.skyfalling.mousika.eval.parser;


import com.skyfalling.mousika.eval.node.CaseNode;
import com.skyfalling.mousika.eval.node.ParNode;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.node.SerNode;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import static com.skyfalling.mousika.eval.parser.Operator.*;


/**
 * 规则解析器，用于生成规则节点树
 */
public class NodeParser {
    private static final int TOK_WORD = 2;
    private static final int TOK_OP = 4;
    private static final int TOK_OPEN = 8;
    private static final int TOK_CLOSE = 16;


    /**
     * @param expression 规则集表达式,形式为规则ID的逻辑组合,如(1||2)&&(3||!4)
     */
    public static RuleNode parse(String expression, NodeGenerator generator) {
        return doParse(tokenize(expression), generator);
    }


    /**
     * 解析表达式
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
                while (!isOpChar(c) && !Character.isWhitespace(c) && pos < input.length()) {
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
     * @param generator 节点生成器
     */
    private static RuleNode doParse(List<String> tokens, Function<String, RuleNode> generator) {
        // 变量栈
        Stack<RuleNode> es = new Stack<>();
        // 操作符栈
        Stack<Operator> os = new Stack<>();
        // 扫描结果
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            Operator right = Operator.of(token);
            //determine op is UNARY_MINUS or MINUS
            if (right == Operator.UNARY_MINUS) {
                if (i > 0) {
                    Operator last = Operator.of(tokens.get(i - 1));
                    //上一个token是操作数或者右括号
                    if (last == null || last == Operator.PAREN_CLOSE) {
                        right = Operator.MINUS;
                    }
                }
            }
            if (right != null) {
                Operator left = os.isEmpty() ? null : os.peek();
                //ordinal越小优先级越高
                while (left != null && isPrecede(right, left) < 0) {
                    es.push(doOperate(es, os));
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
            Operator op = os.peek();
            if (op == Operator.PAREN_OPEN || op == Operator.PAREN_CLOSE) {
                return null; // Bad paren
            }
            RuleNode e = doOperate(es, os);
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
     */
    private static RuleNode doOperate(Stack<RuleNode> es, Stack<Operator> os) {
        Operator op = os.pop();
        try {
            //右操作数
            RuleNode b = es.pop();
            //左操作数
            RuleNode a = op.getArgCount() > 1 ? es.pop() : null;
            switch (op) {
                case LOGICAL_AND:
                    return a.and(b);
                case LOGICAL_OR:
                    return a.or(b);
                case UNARY_LOGICAL_NOT:
                    return b.not();
                case QUESTION_MARK:
                    return new CaseNode(a, b);
                case SINGLE_ARROW:
                    if (a instanceof SerNode) {
                        return ((SerNode) a).next(b);
                    } else {
                        return new SerNode(a, b);
                    }
                case DOUBLE_ARROW:
                    if (a instanceof ParNode) {
                        return ((ParNode) a).next(b);
                    } else {
                        return new ParNode(a, b);
                    }
                case COLON:
                    //a?b:c语法，提取操作符?
                    os.pop();
                    RuleNode t = es.pop();
                    return new CaseNode(t, a, b);
                default:
                    //Unsupported Operator
                    throw new UnsupportedOperationException("Unsupported operator:" + op);
            }
        } catch (EmptyStackException e) {
            throw new IllegalArgumentException("Missing operand:" + op);
        }
    }


}
