package com.skyfalling.mousika.eval.parser;

/**
 * 运算符枚举定义
 *
 * @author liyifei
 */
public enum Operator {
    PAREN_OPEN("(", 0),
    UNARY_MINUS("-", 1, 1, false),
    UNARY_LOGICAL_NOT("!", 1, 1, false),
    UNARY_BITWISE_NOT("~", 1, 1, false),

    POWER("**", 2, 2, false),
    MULTIPLY("*", 3),
    DIVIDE("/", 3),
    REMAINDER("%", 3),

    PLUS("+", 4),
    MINUS("-", 4),

    SHL("<<", 5),
    SHR(">>", 5),

    LT("<", 6),
    LE("<=", 6),
    GT(">", 6),
    GE(">=", 6),
    EQ("==", 6),
    NE("!=", 6),

    BITWISE_AND("&", 7),
    BITWISE_OR("|", 7),
    BITWISE_XOR("^", 7),

    LOGICAL_AND("&&", 8),
    LOGICAL_OR("||", 9),

    COLON(":", 10),
    QUESTION_MARK("?", 11),

    DOUBLE_ARROW("=>", 12),
    SINGLE_ARROW("->", 13),

    ASSIGN("=", 14, false),
    COMMA(",", 15, false),
    SEMICOLON(";", 16, false),
    PAREN_CLOSE(")", 17);

    private final String expr;
    private final int priority;
    private final int argCount;
    private final boolean leftAssoc;

    /**
     * @param expr     运算符号
     * @param priority 优先级
     */
    Operator(String expr, int priority) {
        this(expr, priority, 2, true);
    }

    /**
     * @param expr      运算符号
     * @param priority  优先级
     * @param leftAssoc 是否从左向右计算
     */
    Operator(String expr, int priority, boolean leftAssoc) {
        this(expr, priority, 2, leftAssoc);
    }


    /**
     * @param expr      运算符号
     * @param priority  优先级
     * @param argCount  参数个数
     * @param leftAssoc 是否从左向右计算
     */
    Operator(String expr, int priority, int argCount, boolean leftAssoc) {
        this.expr = expr;
        this.priority = priority;
        this.argCount = argCount;
        this.leftAssoc = leftAssoc;
    }


    public int getArgCount() {
        return argCount;
    }

    /**
     * 比较运算符优先级
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
        if (left.priority == right.priority) {
            return left.leftAssoc ? -1 : 1;
        }
        return right.priority < left.priority ? 1 : -1;
    }

    @Override
    public String toString() {
        return name() + "(" + expr + ')';
    }

    /**
     * 获取操作符
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
     */
    public static boolean isOpChar(char c) {
        return "+-*/%<>=!^&|,(){}[]?:;".indexOf(c) != -1;
    }

}