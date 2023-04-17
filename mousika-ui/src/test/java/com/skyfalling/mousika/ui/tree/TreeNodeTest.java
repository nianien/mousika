package com.skyfalling.mousika.ui.tree;

import com.skyfalling.mousika.ui.tree.node.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created on 2022/6/27
 *
 * @author liyifei
 */
public class TreeNodeTest {

    @Test
    @SneakyThrows
    public void testNode() {
        TreeNode tree = TreeNode.create();
        JNode jNode = new JNode();
        PNode l1 = PNode.of("201");
        PNode l1_1 = PNode.of("202");
        l1_1.setAction(ANode.of("104"));
        l1.addBranch(l1_1);
        l1.setAction(ANode.of("105"));
        jNode.addBranch(l1);
        PNode l2 = PNode.of("203");
        l2.setAction(ANode.of("107"));
        jNode.addBranch(l2);
        jNode.setAction(ANode.of("106"));
        tree.addFlow(jNode);
        System.out.println(TreeAdapter.toRule(tree).expr());
    }

    @Test
    @SneakyThrows
    public void testNode2() {
        TreeNode tree = TreeNode.create();
        JNode jNode = new JNode();
        PNode l1 = PNode.of("207");
        l1.setAction(ANode.of("117"));
        PNode l1_1 = PNode.of("208");
        l1.addBranch(l1_1);
        l1_1.setAction(ANode.of("105"));
        PNode l1_1_1 = PNode.of("202");
        l1_1_1.setAction(ANode.of("104"));
        l1_1.addBranch(l1_1_1);
        jNode.addBranch(l1);
        tree.addFlow(jNode);
        System.out.println(TreeAdapter.toRule(tree).expr());
    }

    @Test
    @SneakyThrows
    public void testBuildTree() {
        TreeNode tree = TreeNode.create();
        tree.addFlow(FNode.of("102"));
        tree.addFlow(FNode.of("103"));
        {
            JNode jNode = new JNode();
            PNode l1 = PNode.of("201");
            PNode l1_1 = PNode.of("202");
            l1_1.setAction(ANode.of("104"));
            l1.addBranch(l1_1);
            l1.setAction(ANode.of("105"));
            jNode.addBranch(l1);
            jNode.setAction(ANode.of("106"));
            tree.addFlow(jNode);
        }

        {
            JNode jNode = new JNode();
            PNode l1 = PNode.of("203");
            l1.setAction(ANode.of("108"));
            jNode.addBranch(l1);
            jNode.setAction(ANode.of("109"));
            tree.addFlow(jNode);
        }

        tree.addFlow(FNode.of("111"));
        tree.addFlow(FNode.of("112"));
        tree.addFlow(FNode.of("113"));
        {
            JNode jNode = new JNode();
            PNode l1 = PNode.of("204");
            l1.setAction(ANode.of("114"));
            jNode.addBranch(l1);
            jNode.setAction(ANode.of("115"));
            tree.addFlow(jNode);
        }

        {
            JNode jNode = new JNode();
            PNode l1 = PNode.of("205");
            l1.setAction(ANode.of("117"));
            PNode l2 = PNode.of("206");
            l2.setAction(ANode.of("118"));
            jNode.addBranch(l1);
            jNode.addBranch(l2);
            tree.addFlow(jNode);
        }

        {
            JNode jNode = new JNode();
            PNode l1 = PNode.of("207");
            l1.setAction(ANode.of("117"));
            PNode l1_1 = PNode.of("208");
            l1.addBranch(l1_1);
            l1_1.setAction(ANode.of("105"));
            PNode l1_1_1 = PNode.of("202");
            l1_1_1.setAction(ANode.of("104"));
            l1_1.addBranch(l1_1_1);
            jNode.addBranch(l1);
            tree.addFlow(jNode);
        }
        String json = tree.toJson();
        System.out.println(json);
        assertEquals(TreeNode.fromJson(json).toJson(), json);
        String expr = TreeAdapter.toRule(tree).expr();
        System.out.println(expr);
        assertEquals(json, TreeAdapter.fromRule(expr).toJson());
    }

//
//    @Test
//    @SneakyThrows
//    public void testFromDecisionNode() {
//        //1&&2
//        GNode l1 = GNode.and().addRule(RNode.of("1")).addRule(RNode.of("2"));
//        PNode l2_1 = PNode.of("3");
//        l2_1.setAction(ANode.of("a"));
//        l1.addBranch(l2_1);
//
//        PNode l2_2 = PNode.of("4");
//        l2_2.setAction(ANode.of("b"));
//        l1.addBranch(l2_2);
//        l1.setAction(ANode.of("c"));
//
//        TreeNode ruleTree = TreeNode.create();
//        ruleTree.addBranch(l1);
//        ruleTree.setAction(ANode.of("c"));
//        String originJson = ruleTree.toJson();
//        System.out.println(originJson);
//        RuleNode actionNode = TreeAdapter.toRule(ruleTree);
//        TreeNode ruleTree2 = TreeAdapter.fromRule(actionNode);
//        String convertJson = ruleTree2.toJson();
//        System.out.println(convertJson);
//        assertEquals(originJson, convertJson);
//    }
//
//    @SneakyThrows
//    @ParameterizedTest
//    @CsvSource(
//            value = {
//                    "(1&&2)?3?4?(!5&&(6||7))?!(8&&9)?a:b:(10||11)?c:12?d:e#NodeResult(expr='(1&&2)?3?4?(!5&&(6||7))?!(8&&9)?a:b:(10||11)?c:12?d:e', result=null, " +
//                            "details=[RuleResult(ruleId=1,result=true,desc='规则1'), " +
//                            "RuleResult(ruleId=2,result=true,desc='规则2'), " +
//                            "RuleResult(ruleId=3,result=true,desc='规则3'), " +
//                            "RuleResult(ruleId=4,result=true,desc='规则4'), " +
//                            "RuleResult(ruleId=5,result=false,desc='规则5'), " +
//                            "RuleResult(ruleId=6,result=true,desc='规则6'), " +
//                            "RuleResult(ruleId=8,result=true,desc='规则8'), " +
//                            "RuleResult(ruleId=9,result=true,desc='规则9'), " +
//                            "RuleResult(ruleId=b,result=null,desc='操作b')])"
//            }, delimiter = '#'
//    )
//    public void test(String expr, String result) {
//        //1&&2
//        GNode l1 = GNode.and().addRule(RNode.of("1")).addRule(RNode.of("2"));
//        PNode l2_1 = PNode.of("3");
//        PNode l3_1 = PNode.of("4");
//
//        //5&&(6||7)
//        GNode l4_1 = GNode.and().addRule(RNode.of("5").negative())
//                .addRule(LNode.or().addRule(RNode.of("6")).addRule(RNode.of("7")));
//
//        //8&&9
//        GNode l5_1 = GNode.and().addRule(RNode.of("8")).addRule(RNode.of("9")).negative();
//        l5_1.setAction(ANode.of("a"));
//        l4_1.addBranch(l5_1);
//        l4_1.setAction(ANode.of("b"));
//        l3_1.addBranch(l4_1);
//        l2_1.addBranch(l3_1);
//
//        //10||11
//        GNode l3_2 = GNode.or().addRule(RNode.of("10")).addRule(RNode.of("11"));
//        l3_2.setAction(ANode.of("c"));
//        l2_1.addBranch(l3_2);
//
//        PNode l2_2 = PNode.of("12");
//        l2_2.setAction(ANode.of("d"));
//        l1.addBranch(l2_1).addBranch(l2_2);
//
//        TreeNode ruleTree = TreeNode.create();
//        ruleTree.addBranch(l1);
//        ruleTree.setAction(ANode.of("e"));
//        String json = ruleTree.toJson();
//        System.out.println(json);
//        ruleTree = TreeNode.fromJson(json);
//        json = ruleTree.toJson();
//        System.out.println(json);
//        String realExpr = TreeAdapter.toRule(ruleTree).expr();
//        System.out.println("规则表达式:" + realExpr);
//
//        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
//                Arrays.asList(
//                        new RuleDefinition("1", "true", "规则1"),
//                        new RuleDefinition("2", "true", "规则2"),
//                        new RuleDefinition("3", "true", "规则3"),
//                        new RuleDefinition("4", "true", "规则4"),
//                        new RuleDefinition("5", "false", "规则5"),
//                        new RuleDefinition("6", "true", "规则6"),
//                        new RuleDefinition("7", "true", "规则7"),
//                        new RuleDefinition("8", "true", "规则8"),
//                        new RuleDefinition("9", "true", "规则9"),
//                        new RuleDefinition("10", "true", "规则10"),
//                        new RuleDefinition("11", "true", "规则11"),
//                        new RuleDefinition("12", "true", "规则12"),
//                        new RuleDefinition("a", "act('a')", "操作a"),
//                        new RuleDefinition("b", "act('b')", "操作b"),
//                        new RuleDefinition("c", "act('c')", "操作c"),
//                        new RuleDefinition("d", "act('d')", "操作d"),
//                        new RuleDefinition("e", "act('e')", "操作e")
//                ),
//                Arrays.asList(
//                        new UdfDefinition("act", new ActionUdf())
//                ));
//
//        RuleEvaluator ruleEvaluator = simpleRuleLoader.loadSuite().getRuleEvaluator();
//        NodeResult realResult1 = ruleEvaluator.eval(expr, null);
//        System.out.println(realResult1);
//        NodeResult realResult2 = ruleEvaluator.eval(realExpr, null);
//        System.out.println(realResult2);
//        assertEquals(realResult1.toString(), result);
//    }
//
//
//    @ParameterizedTest
//    @CsvSource(
//            value = {
//                    "(r1||r2)&&(r3||!(r4||(r5&&r6)))?action1:expr2?expr3?action3:expr4?action4:action2:action0"
//                            + "#NodeResult(expr='((r1||r2)&&(r3||!(r4||(r5&&r6))))?action1:expr2?expr3?action3:expr4?action4:action2:action0', result=null, " +
//                            "details=[RuleResult(ruleId=r1,result=true,desc='规则1'), " +
//                            "RuleResult(ruleId=r3,result=false,desc='规则3'), " +
//                            "RuleResult(ruleId=r4,result=true,desc='规则4'), " +
//                            "RuleResult(ruleId=expr2,result=true,desc='分支2'), " +
//                            "RuleResult(ruleId=expr3,result=false,desc='分支3'), " +
//                            "RuleResult(ruleId=expr4,result=true,desc='分支4'), " +
//                            "RuleResult(ruleId=action4,result=null,desc='操作4')])#{\"type\":\"T\","
//                            + "\"branches\":[{\"type\":\"G\",\"expr\":\"&&\",\"negative\":false,\"branches\":[],"
//                            + "\"action\":{\"type\":\"A\",\"expr\":\"action1\"},\"rules\":[{\"type\":\"L\","
//                            + "\"expr\":\"||\",\"negative\":false,\"rules\":[{\"type\":\"R\",\"expr\":\"r1\","
//                            + "\"negative\":false},{\"type\":\"R\",\"expr\":\"r2\",\"negative\":false}]},"
//                            + "{\"type\":\"L\",\"expr\":\"||\",\"negative\":false,\"rules\":[{\"type\":\"R\","
//                            + "\"expr\":\"r3\",\"negative\":false},{\"type\":\"L\",\"expr\":\"||\",\"negative\":true,"
//                            + "\"rules\":[{\"type\":\"R\",\"expr\":\"r4\",\"negative\":false},{\"type\":\"L\","
//                            + "\"expr\":\"&&\",\"negative\":false,\"rules\":[{\"type\":\"R\",\"expr\":\"r5\","
//                            + "\"negative\":false},{\"type\":\"R\",\"expr\":\"r6\",\"negative\":false}]}]}]}]},"
//                            + "{\"type\":\"P\",\"expr\":\"expr2\",\"negative\":false,\"branches\":[{\"type\":\"P\","
//                            + "\"expr\":\"expr3\",\"negative\":false,\"branches\":[],\"action\":{\"type\":\"A\","
//                            + "\"expr\":\"action3\"}},{\"type\":\"P\",\"expr\":\"expr4\",\"negative\":false,"
//                            + "\"branches\":[],\"action\":{\"type\":\"A\",\"expr\":\"action4\"}}],"
//                            + "\"action\":{\"type\":\"A\",\"expr\":\"action2\"}}],\"action\":{\"type\":\"A\","
//                            + "\"expr\":\"action0\"}}"
//            }, delimiter = '#'
//    )
//    public void test2(String expr, String res, String tree) {
//        TreeNode treeNode = TreeAdapter.fromRule(expr);
//        String jsonTree = treeNode.toJson();
//        System.out.println(jsonTree);
//        assertEquals(tree, jsonTree);
//        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
//                Arrays.asList(
//                        new RuleDefinition("r1", "true", "规则1"),
//                        new RuleDefinition("r2", "true", "规则2"),
//                        new RuleDefinition("r3", "false", "规则3"),
//                        new RuleDefinition("r4", "true", "规则4"),
//                        new RuleDefinition("r5", "true", "规则5"),
//                        new RuleDefinition("r6", "true", "规则6"),
//                        new RuleDefinition("expr2", "true", "分支2"),
//                        new RuleDefinition("expr3", "false", "分支3"),
//                        new RuleDefinition("expr4", "true", "分支4"),
//                        new RuleDefinition("action0", "act('0')", "操作0"),
//                        new RuleDefinition("action1", "act('1')", "操作1"),
//                        new RuleDefinition("action2", "act('2')", "操作2"),
//                        new RuleDefinition("action3", "act('3')", "操作3"),
//                        new RuleDefinition("action4", "act('4')", "操作4")
//                ),
//                Arrays.asList(
//                        new UdfDefinition("act", new ActionUdf())
//                ));
//
//        RuleEvaluator ruleEvaluator = simpleRuleLoader.loadSuite().getRuleEvaluator();
//        NodeResult result = ruleEvaluator.eval(expr, null);
//        System.out.println(result);
//        assertEquals(res, result.toString());
//    }
//
//
//    @Test
//    @SneakyThrows
//    public void testVisit() {
//        //1&&2
//        GNode l1 = GNode.and().addRule(RNode.of("1")).addRule(RNode.of("2"));
//        PNode l2_1 = PNode.of("3");
//        PNode l3_1 = PNode.of("4");
//
//        //5&&(6||7)
//        GNode l4_1 = GNode.and().addRule(RNode.of("5").negative())
//                .addRule(LNode.or().addRule(RNode.of("6")).addRule(RNode.of("7")));
//
//        //8&&9
//        GNode l5_1 = GNode.and().addRule(RNode.of("8")).addRule(RNode.of("9")).negative();
//        l5_1.setAction(ANode.of("17"));
//        l4_1.addBranch(l5_1);
//        l4_1.setAction(ANode.of("13"));
//        l3_1.addBranch(l4_1);
//        l2_1.addBranch(l3_1);
//
//        //10||11
//        GNode l3_2 = GNode.or().addRule(RNode.of("10")).addRule(RNode.of("11"));
//        l3_2.setAction(ANode.of("14"));
//        l2_1.addBranch(l3_2);
//
//        l1.addBranch(l2_1);
//
//        PNode l2_2 = PNode.of("12");
//        l2_2.setAction(ANode.of("15"));
//        l1.addBranch(l2_2);
//
//        TreeNode ruleTree = TreeNode.create();
//        ruleTree.addBranch(l1);
//        ruleTree.setAction(ANode.of("16"));
//        String json = ruleTree.toJson();
//        System.out.println(json);
//        Set<Long> idSet = ruleTree.visit(NodeVisitors.RULE_ID_TO_LONG_COLLECTOR, HashSet::new);
//
//        Set<Long> expectedSet = IntStream.rangeClosed(1, 17).mapToObj(Long::valueOf).collect(Collectors.toSet());
//        assertEquals(expectedSet, idSet);
//    }
//
//    @Test
//    public void testTreeFromExpr() {
//        String expr = "(107)?((122||121)?348:349):null";
//        TreeNode tree = TreeAdapter.fromRule(expr);
//        assertDoesNotThrow(() -> tree.visit(NodeVisitors.RULE_ID_TO_LONG_COLLECTOR));
//    }
//
//    @Test
//    public void testNegative() {
//        String expr = "(1&&!2)?3:4";
//        TreeNode from = TreeAdapter.fromRule(expr);
//        System.out.println(from.toJson());
//        //        String actual = from.expr();
//        //        System.out.println(actual);
//        //        assertEquals(expr, actual);
//    }
//
//    @Test
//    public void testLNodeNegative() {
//        String expr = "!(1&&2)?3:4";
//        TreeNode from = TreeAdapter.fromRule(expr);
//        System.out.println(from.toJson());
//        String actual = TreeAdapter.toRule(from).expr();
//        System.out.println(actual);
//        assertEquals(expr, actual);
//    }
//
//    @Test
//    public void testRuleCheckVisitor() {
//        String expr = "(107)?((122||121)?348:349):null";
//        TreeNode tree = TreeAdapter.fromRule(NodeBuilder.build(expr));
//        assertDoesNotThrow(() -> tree.visit(NodeVisitors.NODE_VALIDATOR));
//    }
//
//    /**
//     * 校验转换之后的 数据是否一致
//     */
//    @Test
//    public void testCheck() {
//        String expr =
//                "(106&&(107||108))?((126&&109&&125&&124&&138&&(122||121)&&(110||(111&&(120||119)))&&((128&&(117||"
//                        + "(118&&(112||113))))||(115&&(117||(118&&(114&&116))))))?348:349):null";
//        RuleNode decisionNode = NodeBuilder.build(expr);
//        TreeNode tree = TreeAdapter.fromRule(decisionNode);
//        String treeExpr = TreeAdapter.toRule(tree).expr();
//        System.out.println("convert expr :" + treeExpr);
//
//        TreeNode newTree = TreeAdapter.fromRule(NodeBuilder.build(treeExpr));
//        assertEquals(tree.toJson(), newTree.toJson());
//    }
//
//    @Test
//    public void checkNoBranch() {
//        String expr = "a";
//        RuleNode ruleNode = NodeBuilder.build(expr);
//        TreeNode tree = TreeAdapter.fromRule(ruleNode);
//        System.out.println(tree.toJson());
//        assertNotNull(tree.getBranches());
//    }
//
//    @Test
//    public void testVisit2() {
//
//        String json = "{\n" +
//                "  \"type\": \"T\",\n" +
//                "  \"branches\": [\n" +
//                "    {\n" +
//                "      \"type\": \"G\",\n" +
//                "      \"expr\": \"&&\",\n" +
//                "      \"negative\": false,\n" +
//                "      \"action\": {\n" +
//                "        \"type\": \"A\",\n" +
//                "        \"expr\": \"action1\"\n" +
//                "      },\n" +
//                "      \"rules\": [\n" +
//                "        {\n" +
//                "          \"type\": \"L\",\n" +
//                "          \"expr\": \"或\",\n" +
//                "          \"negative\": false,\n" +
//                "          \"rules\": [\n" +
//                "            {\n" +
//                "              \"type\": \"R\",\n" +
//                "              \"expr\": \"r1\",\n" +
//                "              \"negative\": false\n" +
//                "            },\n" +
//                "            {\n" +
//                "              \"type\": \"R\",\n" +
//                "              \"expr\": \"r2\",\n" +
//                "              \"negative\": false\n" +
//                "            }\n" +
//                "          ]\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"type\": \"L\",\n" +
//                "          \"expr\": \"或\",\n" +
//                "          \"negative\": false,\n" +
//                "          \"rules\": [\n" +
//                "            {\n" +
//                "              \"type\": \"R\",\n" +
//                "              \"expr\": \"r3\",\n" +
//                "              \"negative\": false\n" +
//                "            },\n" +
//                "            {\n" +
//                "              \"type\": \"L\",\n" +
//                "              \"expr\": \"||\",\n" +
//                "              \"negative\": true,\n" +
//                "              \"rules\": [\n" +
//                "                {\n" +
//                "                  \"type\": \"R\",\n" +
//                "                  \"expr\": \"r4\",\n" +
//                "                  \"negative\": false\n" +
//                "                },\n" +
//                "                {\n" +
//                "                  \"type\": \"L\",\n" +
//                "                  \"expr\": \"&&\",\n" +
//                "                  \"negative\": false,\n" +
//                "                  \"rules\": [\n" +
//                "                    {\n" +
//                "                      \"type\": \"R\",\n" +
//                "                      \"expr\": \"r5\",\n" +
//                "                      \"negative\": false\n" +
//                "                    },\n" +
//                "                    {\n" +
//                "                      \"type\": \"R\",\n" +
//                "                      \"expr\": \"r6\",\n" +
//                "                      \"negative\": false\n" +
//                "                    }\n" +
//                "                  ]\n" +
//                "                }\n" +
//                "              ]\n" +
//                "            }\n" +
//                "          ]\n" +
//                "        }\n" +
//                "      ]\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"type\": \"P\",\n" +
//                "      \"expr\": \"expr2\",\n" +
//                "      \"negative\": false,\n" +
//                "      \"branches\": [\n" +
//                "        {\n" +
//                "          \"type\": \"P\",\n" +
//                "          \"expr\": \"expr3\",\n" +
//                "          \"negative\": false,\n" +
//                "          \"action\": {\n" +
//                "            \"type\": \"A\",\n" +
//                "            \"expr\": \"action3\"\n" +
//                "          }\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"type\": \"P\",\n" +
//                "          \"expr\": \"expr4\",\n" +
//                "          \"negative\": false,\n" +
//                "          \"action\": {\n" +
//                "            \"type\": \"A\",\n" +
//                "            \"expr\": \"action4\"\n" +
//                "          }\n" +
//                "        }\n" +
//                "      ],\n" +
//                "      \"action\": {\n" +
//                "        \"type\": \"A\",\n" +
//                "        \"expr\": \"action2\"\n" +
//                "      }\n" +
//                "    }\n" +
//                "  ],\n" +
//                "  \"action\": {\n" +
//                "    \"type\": \"A\",\n" +
//                "    \"expr\": \"action0\"\n" +
//                "  }\n" +
//                "}";
//        System.out.println(json);
//        assertThrows(IllegalStateException.class, () -> TreeNode.fromJson(json).visit(NodeVisitors.NODE_VALIDATOR))
//                .printStackTrace();
//    }


}
