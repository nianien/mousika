package com.skyfalling.mousika.eval.parser;

/**
 * 运算符枚举定义
 *
 * @author liyifei
 */
public enum Operator {
    PAREN_OPEN("("),
    UNARY_MINUS("-", 1, false),
    UNARY_LOGICAL_NOT("!", 1, false),
    UNARY_BITWISE_NOT("~", 1, false),

    POWER("**", 2, false),
    MULTIPLY("*"),
    DIVIDE("/"),
    REMAINDER("%"),

    PLUS("+"),
    MINUS("+"),

    SHL("<<"),
    SHR(">>"),

    LT("<"),
    LE("<="),
    GT(">"),
    GE(">="),
    EQ("=="),
    NE("!="),

    BITWISE_AND("&"),
    BITWISE_OR("^"),
    BITWISE_XOR("|"),

    LOGICAL_AND("&&"),
    LOGICAL_OR("||"),
    COLON(":", 3, true),
    QUESTION_MARK("?", 0, false),
    ASSIGN("=", 2, false),
    COMMA(",", 2, false),
    PAREN_CLOSE(")");

    private final String expr;
    private final int argNums;
    private final boolean leftAssoc;

    Operator(String expr) {
        this(expr, 2, true);
    }

    Operator(String expr, int argNums, boolean leftAssoc) {
        this.expr = expr;
        this.argNums = argNums;
        this.leftAssoc = leftAssoc;
    }


    public int getArgNums() {
        return argNums;
    }

    /**
     * 比较运算符优先级
     *
     * @param right
     * @param left
     * @return
     */
    public static int isPrecede(Operator right, Operator left) {
        //左右括号匹配时，优先级相等，仅此一例
        if (left == PAREN_OPEN && right == PAREN_CLOSE) {
            return 0;
        }
        //优先计算最右边的"("
        if (left == PAREN_OPEN) {
            return 1;
        }
        //右括号")"优先级最低
        if (right == PAREN_CLOSE) {
            return -1;
        }
        //三元运算符优先级比较
        if (left == Operator.QUESTION_MARK && right == QUESTION_MARK
                || left == Operator.QUESTION_MARK && right == COLON
                || left == Operator.COLON && right == QUESTION_MARK) {
            return 1;
        }
        if (left == Operator.COLON && right == COLON) {
            return -1;
        }
        //优先级相等判断计算方向
        if (left == right) {
            return left.leftAssoc ? -1 : 1;
        }
        return right.ordinal() < left.ordinal() ? 1 : -1;
    }

    /**
     * 获取操作符
     *
     * @param expr
     * @return
     */
    public static Operator of(String expr) {
        for (Operator op : values()) {
            if (op.expr.equals(expr)) {
                return op;
            }
        }
        return null;
    }


    /**
     * 运算符字符
     *
     * @param c
     * @return
     */
    public static boolean isOpChar(char c) {
        return "+-*/%<>=!^&|,(){}[]?:".indexOf(c) != -1;
    }

}