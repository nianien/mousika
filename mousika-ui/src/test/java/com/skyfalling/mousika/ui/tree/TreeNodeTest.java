package com.skyfalling.mousika.ui.tree;

import com.skyfalling.mousika.engine.RuleDefinition;
import com.skyfalling.mousika.engine.UdfDefinition;
import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.result.NodeResult;
import com.skyfalling.mousika.suite.RuleSuite;
import com.skyfalling.mousika.ui.tree.mock.SimpleRuleLoader;
import com.skyfalling.mousika.ui.tree.node.*;
import com.skyfalling.mousika.ui.tree.udf.ActionUdf;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created on 2022/6/27
 *
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
public class TreeNodeTest {

    @Test
    @SneakyThrows
    public void testTreeNode1() {
        TreeNode tree = TreeNode.create();
        ZNode zNode = new ZNode();
        PNode l1 = PNode.of("201");
        PNode l1_1 = PNode.of("202");
        l1_1.setAction(ANode.of("104"));
        l1.addBranch(l1_1);
        l1.setAction(ANode.of("105"));
        zNode.addBranch(l1);
        PNode l2 = PNode.of("203");
        l2.setAction(ANode.of("107"));
        zNode.addBranch(l2);
        zNode.setAction(ANode.of("106"));
        tree.addFlow(zNode);
        System.out.println(TreeAdapter.toRule(tree).expr());
    }

    @Test
    @SneakyThrows
    public void testTreeNode2() {
        TreeNode tree = TreeNode.create();
        ZNode zNode = new ZNode();
        PNode l1 = PNode.of("207");
        l1.setAction(ANode.of("117"));
        PNode l1_1 = PNode.of("208");
        l1.addBranch(l1_1);
        l1_1.setAction(ANode.of("105"));
        PNode l1_1_1 = PNode.of("202");
        ANode a1_1_1 = ANode.of("104");
        l1_1_1.setAction(a1_1_1);
        a1_1_1.addFlow(FNode.of("106"));
        a1_1_1.addFlow(FNode.of("107"));
        l1_1.addBranch(l1_1_1);
        zNode.addBranch(l1);
        tree.addFlow(zNode);
        System.out.println(tree.toJson());
        System.out.println(TreeAdapter.toRule(tree).expr());
    }

    @Test
    @SneakyThrows
    public void testTreeNode3() {
        TreeNode tree = TreeNode.create();
        tree.addFlow(FNode.of("102"));
        tree.addFlow(FNode.of("103"));
        {
            ZNode zNode = new ZNode();
            PNode l1 = PNode.of("201");
            PNode l1_1 = PNode.of("202");
            l1_1.setAction(ANode.of("104"));
            l1.addBranch(l1_1);
            l1.setAction(ANode.of("105"));
            zNode.addBranch(l1);
            zNode.setAction(ANode.of("106"));
            tree.addFlow(zNode);
        }

        {
            ZNode zNode = new ZNode();
            PNode l1 = PNode.of("203");
            l1.setAction(ANode.of("108"));
            zNode.addBranch(l1);
            zNode.setAction(ANode.of("109"));
            tree.addFlow(zNode);
        }

        tree.addFlow(FNode.of("111"));
        tree.addFlow(FNode.of("112"));
        tree.addFlow(FNode.of("113"));
        {
            ZNode zNode = new ZNode();
            PNode l1 = PNode.of("204");
            l1.setAction(ANode.of("114"));
            zNode.addBranch(l1);
            zNode.setAction(ANode.of("115"));
            tree.addFlow(zNode);
        }

        {
            ZNode zNode = new ZNode();
            PNode l1 = PNode.of("205");
            l1.setAction(ANode.of("117"));
            PNode l2 = PNode.of("206");
            l2.setAction(ANode.of("118"));
            zNode.addBranch(l1);
            zNode.addBranch(l2);
            tree.addFlow(zNode);
        }

        {
            ZNode zNode = new ZNode();
            PNode l1 = PNode.of("207");
            l1.setAction(ANode.of("117"));
            PNode l1_1 = PNode.of("208");
            l1.addBranch(l1_1);
            l1_1.setAction(ANode.of("105"));
            PNode l1_1_1 = PNode.of("202");
            l1_1_1.setAction(ANode.of("104"));
            l1_1.addBranch(l1_1_1);
            zNode.addBranch(l1);
            tree.addFlow(zNode);
        }
        String json = tree.toJson();
        System.out.println(json);
        assertEquals(TreeNode.fromJson(json).toJson(), json);
        String expr = TreeAdapter.toRule(tree).expr();
        System.out.println(expr);
        assertEquals(json, TreeAdapter.fromRule(expr).toJson());
    }


    @Test
    @SneakyThrows
    public void testFromRuleNode() {
        //1&&2
        GNode l1 = GNode.and().addRule(RNode.of("1")).addRule(RNode.of("2"));
        PNode l2_1 = PNode.of("3");
        l2_1.setAction(ANode.of("a"));
        l1.addBranch(l2_1);

        PNode l2_2 = PNode.of("4");
        l2_2.setAction(ANode.of("b"));
        l1.addBranch(l2_2);
        l1.setAction(ANode.of("c"));

        ZNode zNode = new ZNode();
        zNode.addBranch(l1);
        zNode.setAction(ANode.of("c"));
        TreeNode ruleTree = TreeNode.create();
        ruleTree.addFlow(zNode);
        String originJson = ruleTree.toJson();
        System.out.println(originJson);
        RuleNode ruleNode = TreeAdapter.toRule(ruleTree);
        TreeNode ruleTree2 = TreeAdapter.fromRule(ruleNode);
        String convertJson = ruleTree2.toJson();
        System.out.println(convertJson);
        assertEquals(originJson, convertJson);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource(
            value = {
                    "(1&&2)?3?4?(!5&&(6||7))?!(8&&9)?a:b:(10||11)?c:12?d:e#b"
            }, delimiter = '#'
    )
    public void testTreeEval(String expr, String result) {
        //1&&2
        GNode l1 = GNode.and().addRule(RNode.of("1")).addRule(RNode.of("2"));
        PNode l2_1 = PNode.of("3");
        PNode l3_1 = PNode.of("4");

        //5&&(6||7)
        GNode l4_1 = GNode.and().addRule(RNode.of("5").negative())
                .addRule(LNode.or().addRule(RNode.of("6")).addRule(RNode.of("7")));

        //8&&9
        GNode l5_1 = GNode.and().addRule(RNode.of("8")).addRule(RNode.of("9")).negative();
        l5_1.setAction(ANode.of("a"));
        l4_1.addBranch(l5_1);
        l4_1.setAction(ANode.of("b"));
        l3_1.addBranch(l4_1);
        l2_1.addBranch(l3_1);

        //10||11
        GNode l3_2 = GNode.or().addRule(RNode.of("10")).addRule(RNode.of("11"));
        l3_2.setAction(ANode.of("c"));
        l2_1.addBranch(l3_2);

        PNode l2_2 = PNode.of("12");
        l2_2.setAction(ANode.of("d"));
        l1.addBranch(l2_1).addBranch(l2_2);

        TreeNode ruleTree = TreeNode.create();
        ZNode zNode = new ZNode();
        zNode.addBranch(l1);
        zNode.setAction(ANode.of("e"));
        ruleTree.addFlow(zNode);
        String json = ruleTree.toJson();
        System.out.println(json);
        ruleTree = TreeNode.fromJson(json);
        json = ruleTree.toJson();
        System.out.println(json);
        String realExpr = TreeAdapter.toRule(ruleTree).expr();
        System.out.println("规则表达式:" + realExpr);
        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(
                        new RuleDefinition("1", "true", "规则1"),
                        new RuleDefinition("2", "true", "规则2"),
                        new RuleDefinition("3", "true", "规则3"),
                        new RuleDefinition("4", "true", "规则4"),
                        new RuleDefinition("5", "false", "规则5"),
                        new RuleDefinition("6", "true", "规则6"),
                        new RuleDefinition("7", "true", "规则7"),
                        new RuleDefinition("8", "true", "规则8"),
                        new RuleDefinition("9", "true", "规则9"),
                        new RuleDefinition("10", "true", "规则10"),
                        new RuleDefinition("11", "true", "规则11"),
                        new RuleDefinition("12", "true", "规则12"),
                        new RuleDefinition("a", "act('a')", "操作a"),
                        new RuleDefinition("b", "act('b')", "操作b"),
                        new RuleDefinition("c", "act('c')", "操作c"),
                        new RuleDefinition("d", "act('d')", "操作d"),
                        new RuleDefinition("e", "act('e')", "操作e")
                ),
                Arrays.asList(
                        new UdfDefinition("act", new ActionUdf())
                ));

        RuleSuite ruleSuite = simpleRuleLoader.loadSuite();
        NodeResult actualResult = ruleSuite.evalExpr(expr, null);
        System.out.println(actualResult);
        assertEquals(result, actualResult.getResult());
    }


    @ParameterizedTest
    @CsvSource(
            value = {
                    "(r1||r2)&&(r3||!(r4||(r5&&r6)))?action1:expr2?expr3?action3:expr4?action4:action2:action0#4"
            }, delimiter = '#'
    )
    public void testTreeEval2(String expr, String res) {
        TreeNode treeNode = TreeAdapter.fromRule(expr);
        String jsonTree = treeNode.toJson();
        System.out.println(jsonTree);
        SimpleRuleLoader simpleRuleLoader = new SimpleRuleLoader(
                Arrays.asList(
                        new RuleDefinition("r1", "true", "规则1"),
                        new RuleDefinition("r2", "true", "规则2"),
                        new RuleDefinition("r3", "false", "规则3"),
                        new RuleDefinition("r4", "true", "规则4"),
                        new RuleDefinition("r5", "true", "规则5"),
                        new RuleDefinition("r6", "true", "规则6"),
                        new RuleDefinition("expr2", "true", "分支2"),
                        new RuleDefinition("expr3", "false", "分支3"),
                        new RuleDefinition("expr4", "true", "分支4"),
                        new RuleDefinition("action0", "act('0')", "操作0"),
                        new RuleDefinition("action1", "act('1')", "操作1"),
                        new RuleDefinition("action2", "act('2')", "操作2"),
                        new RuleDefinition("action3", "act('3')", "操作3"),
                        new RuleDefinition("action4", "act('4')", "操作4")
                ),
                Arrays.asList(
                        new UdfDefinition("act", new ActionUdf())
                ));

        RuleSuite ruleSuite = simpleRuleLoader.loadSuite();
        NodeResult result = ruleSuite.evalExpr(expr, null);
        System.out.println(result);
        assertEquals(res, result.getResult());
    }


    @ParameterizedTest
    @CsvSource(
            value = {
                    "(!(1&&2)?3:4)#{\"type\":\"T\",\"expr\":\"\",\"flows\":[{\"type\":\"Z\",\"branches\":[{\"type\":\"G\",\"expr\":\"&&\",\"branches\":[],\"action\":{\"type\":\"A\",\"expr\":\"3\",\"flows\":[]},\"negative\":true,\"rules\":[{\"type\":\"R\",\"expr\":\"1\",\"negative\":false},{\"type\":\"R\",\"expr\":\"2\",\"negative\":false}]}],\"action\":{\"type\":\"A\",\"expr\":\"4\",\"flows\":[]}}]}"
                    , "((1&&!2)?3:4)#{\"type\":\"T\",\"expr\":\"\",\"flows\":[{\"type\":\"Z\",\"branches\":[{\"type\":\"G\",\"expr\":\"&&\",\"branches\":[],\"action\":{\"type\":\"A\",\"expr\":\"3\",\"flows\":[]},\"negative\":false,\"rules\":[{\"type\":\"R\",\"expr\":\"1\",\"negative\":false},{\"type\":\"R\",\"expr\":\"2\",\"negative\":true}]}],\"action\":{\"type\":\"A\",\"expr\":\"4\",\"flows\":[]}}]}"
                    , "a#{\"type\":\"T\",\"expr\":\"\",\"flows\":[{\"type\":\"F\",\"expr\":\"a\"}]}"

            }, delimiter = '#'
    )
    public void testTreeAdapter(String expr, String jsonTree) {
        TreeNode from = TreeAdapter.fromRule(expr);
        String json = from.toJson();
        System.out.println(json);
        RuleNode ruleNode = TreeAdapter.toRule(from);
        String ruleExpr = ruleNode.expr();
        System.out.println(ruleExpr);
        assertEquals(ruleExpr, expr);
        assertEquals(jsonTree, json);
    }


    @Test
    public void testTreeValidate() {
        TreeNode tree = TreeNode.create();
        ZNode zNode = new ZNode();
        PNode l1 = PNode.of("201");
        PNode l1_1 = PNode.of("202");
        l1_1.setAction(ANode.of("104"));
        l1.addBranch(l1_1);
        l1.setAction(ANode.of(""));
        zNode.addBranch(l1);
        PNode l2 = PNode.of("203");
        l2.setAction(ANode.of("107"));
        zNode.addBranch(l2);
        zNode.setAction(ANode.of("106"));
        tree.addFlow(zNode);
        String expr = TreeAdapter.toRule(tree).expr();
        System.out.println(expr);
        assertEquals(expr, "(201?(202?104):(203?107:106))");
        assertThrows(IllegalStateException.class, () -> tree.validate());
    }

    @Test
    @SneakyThrows
    public void testVisit() {
        TreeNode ruleTree = TreeAdapter.fromRule("(207?(208?(202?104:105):117))");
        Set<String> idSet = ruleTree.collect();
        Set<String> expectedSet = new HashSet<>(Arrays.asList("207", "208", "202", "104", "105", "117"));
        assertEquals(expectedSet, idSet);
    }


}
