package com.skyfalling.mousika.ui.tree2;

import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.parser.NodeBuilder;
import com.skyfalling.mousika.ui.tree2.node.TreeNode;
import com.skyfalling.mousika.ui.tree2.node.flow.*;
import com.skyfalling.mousika.ui.tree2.node.rule.LNode;
import com.skyfalling.mousika.ui.tree2.node.rule.RNode;
import com.skyfalling.mousika.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created on 2023/4/24
 *
 * @author liyifei
 */
public class TreeNodeTest {

    @Test
    public void testFlow() {
        TreeNode tree = new TreeNode();
        ANode a1 = new ANode("a1");
        ANode a2 = new ANode("a2");
        CNode c3 = new CNode("c3");
        ANode a3 = new ANode("a3");
        ANode a4 = new ANode("a4");
        tree.setNext(a1);
        a1.setNext(a2);
        a2.setNext(c3);
        c3.setAction(a3);
        a3.setNext(a4);
        RuleNode rule = tree.toRule();
        System.out.println(rule);
        TreeNode t2 = new TreeNode().fromRule(rule);
        System.out.println(JsonUtils.toJson(t2));
        assertEquals("{\"type\":\"T\",\"expr\":\"\",\"next\":{\"type\":\"A\",\"expr\":\"a1\",\"next\":{\"type\":\"A\",\"expr\":\"a2\",\"next\":{\"type\":\"C\",\"expr\":\"c3\",\"negative\":false,\"action\":{\"type\":\"A\",\"expr\":\"a3\",\"next\":{\"type\":\"A\",\"expr\":\"a4\"}}}}}}",
                JsonUtils.toJson(t2));
    }


    @Test
    public void testDecision() {
        TreeNode tree = new TreeNode();
        ANode a0 = new ANode("a0");
        tree.setNext(a0);
        DNode d1 = new DNode();
        a0.setNext(d1);
        CNode c1 = new CNode("c1");
        c1.setAction(new ANode("a1"));
        d1.addBranch(c1);
        CNode c2 = new CNode("c2");
        c2.setAction(new ANode("a2"));
        d1.addBranch(c2);
        d1.setAction(new ANode("a3"));
        RuleNode rule = tree.toRule();
        System.out.println(rule);
        System.out.println(JsonUtils.toJson(tree));
    }


    @Test
    public void testSerial() {
        TreeNode tree = new TreeNode();
        SNode s1 = new SNode();
        tree.setNext(s1);
        CNode c1 = new CNode("c1");
        c1.setAction(new ANode("a1"));
        s1.addBranch(c1);
        CNode c2 = new CNode("c2");
        c2.setAction(new ANode("a2"));
        s1.addBranch(c2);
        CNode c3 = new CNode("c3");
        c3.setAction(new ANode("a3"));
        s1.addBranch(c3);

        TreeNode t2 = JsonUtils.toBean(JsonUtils.toJson(tree), TreeNode.class);
        RuleNode rule = tree.toRule();
        RuleNode rule2 = t2.toRule();
        TreeNode t3 = new TreeNode().fromRule(rule2);
        System.out.println(rule2);
        System.out.println(JsonUtils.toJson(tree));
        System.out.println(JsonUtils.toJson(t2));
        System.out.println(JsonUtils.toJson(t3));
        assertEquals(rule.expr(), rule2.expr());
        assertEquals(JsonUtils.toJson(tree), JsonUtils.toJson(t3));
    }

    @Test
    public void testSerial2() {
        TreeNode tree = new TreeNode();
        SNode s1 = new SNode();
        tree.setNext(s1);
        ANode a1 = new ANode("a1");
        ANode a2 = new ANode("a2");
        a1.setNext(a2);
        s1.addBranch(a1);
        ANode a3 = new ANode("a3");
        ANode a4 = new ANode("a4");
        a3.setNext(a4);
        s1.addBranch(a3);
        RuleNode rule = tree.toRule();
        System.out.println(rule);
        assertEquals("nop->(a1->a2)->(a3->a4)", rule.expr());
        TreeNode t2 = new TreeNode().fromRule(rule);
        System.out.println(JsonUtils.toJson(t2));
        assertEquals(JsonUtils.toJson(tree), JsonUtils.toJson(t2));
    }

    @Test
    public void testParallel() {
        TreeNode tree = new TreeNode();
        PNode p1 = new PNode();
        ANode a0 = new ANode("a0");
        tree.setNext(a0);
        a0.setNext(p1);
        p1.addBranch(new ANode("a1"));
        p1.addBranch(new ANode("a2"));
        p1.addBranch(new ANode("a3"));
        System.out.println(tree.toRule());
    }

    @Test
    public void testJudge() {
        TreeNode tree = new TreeNode();
        ANode a0 = new ANode("a0");
        tree.setNext(a0);
        DNode d1 = new DNode();
        a0.setNext(d1);
        JNode j1 = new JNode();
        j1.setRule(LNode.and()
                .addRule(LNode.or().addRule(new RNode("r1")).addRule(new RNode("r2")))
                .addRule(LNode.or().addRule(new RNode("r3")).addRule(new RNode("r4"))));
        j1.setAction(new ANode("a1"));
        d1.addBranch(j1);

        CNode c2 = new CNode("c2");
        c2.setAction(new ANode("a2"));
        d1.addBranch(c2);
        d1.setAction(new ANode("a3"));
        RuleNode rule = JsonUtils.toBean(JsonUtils.toJson(tree), TreeNode.class).toRule();
        TreeNode t2 = new TreeNode().fromRule(rule);
        System.out.println(JsonUtils.toJson(t2));
        System.out.println(rule);
        System.out.println(t2.toRule());
        assertEquals(JsonUtils.toJson(tree), JsonUtils.toJson(t2));
        assertEquals(rule.expr(), t2.toRule().expr());
    }

    @Test
    public void testFromCaseNode() {
        RuleNode caseNode = NodeBuilder.build("c1?a1:c2&&!c3?a2:a3");
        System.out.println(JsonUtils.toJson(new TreeNode().fromRule(caseNode)));
    }

    @Test
    public void testFromLogicNode() {
        RuleNode caseNode = NodeBuilder.build("c1&&!c2");
        System.out.println(JsonUtils.toJson(new TreeNode().fromRule(caseNode)));
    }

    @Test
    public void testFromSerNode() {
        RuleNode caseNode = NodeBuilder.build("c1->c2->c3");
        System.out.println(JsonUtils.toJson(new TreeNode().fromRule(caseNode)));
    }


    @Test
    public void testFromExprNode() {
        RuleNode rule = NodeBuilder.build("c1");
        TreeNode t1 = new TreeNode().fromRule(rule);
        System.out.println(JsonUtils.toJson(t1));
        RuleNode rule2 = NodeBuilder.build("c1?nop");
        TreeNode t2 = new TreeNode().fromRule(rule2);
        System.out.println(JsonUtils.toJson(t2));
    }


}
