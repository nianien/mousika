package com.skyfalling.mousika.ui.tree2;

import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.parser.NodeBuilder;
import com.skyfalling.mousika.ui.tree2.node.flow.CNode;
import com.skyfalling.mousika.ui.tree2.node.flow.JNode;
import com.skyfalling.mousika.ui.tree2.node.flow.*;
import com.skyfalling.mousika.ui.tree2.node.rule.LNode;
import com.skyfalling.mousika.ui.tree2.node.rule.RNode;
import com.skyfalling.mousika.utils.JsonUtils;
import org.junit.jupiter.api.Test;

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
//        CNode c3 = new CNode("c3");
        ANode a4 = new ANode("a4");
        tree.setNext(a1);
        a1.setNext(a2);
//        a2.setNext(c3);
//        c3.setNext(a4);
        System.out.println(tree.toRule());
        System.out.println(JsonUtils.toJson(new TreeNode().fromRule(tree.toRule())));
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
        System.out.println(JsonUtils.toJson(tree));
        System.out.println(tree.toRule());
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
        System.out.println(JsonUtils.toJson(tree));
        TreeNode t2=JsonUtils.toBean("{\n" +
                "  \"type\": \"T\",\n" +
                "  \"expr\": \"\",\n" +
                "  \"next\": {\n" +
                "    \"type\": \"S\",\n" +
                "    \"expr\": \"+\",\n" +
                "    \"branches\": [\n" +
                "      {\n" +
                "        \"type\": \"C\",\n" +
                "        \"expr\": \"c1\",\n" +
                "        \"negative\": false,\n" +
                "        \"action\": {\n" +
                "          \"type\": \"A\",\n" +
                "          \"expr\": \"a1\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"C\",\n" +
                "        \"expr\": \"c2\",\n" +
                "        \"negative\": false,\n" +
                "        \"action\": {\n" +
                "          \"type\": \"A\",\n" +
                "          \"expr\": \"a2\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"C\",\n" +
                "        \"expr\": \"c3\",\n" +
                "        \"negative\": false,\n" +
                "        \"action\": {\n" +
                "          \"type\": \"A\",\n" +
                "          \"expr\": \"a3\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"rule2UI\": {},\n" +
                "  \"ui2Rule\": {}\n" +
                "}",TreeNode.class);
        System.out.println(JsonUtils.toJson(t2));
        System.out.println(JsonUtils.toJson(new TreeNode().fromRule(tree.toRule())));
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
        System.out.println(tree.toRule());
        System.out.println(JsonUtils.toJson(new TreeNode().fromRule(tree.toRule())));
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
        System.out.println(tree.toRule());
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
//        RuleNode caseNode = NodeBuilder.build("c1?a1");
//        TreeNode treeNode = new TreeNode().fromRule(caseNode);
//        System.out.println(JsonUtils.toJson(treeNode));
//        System.out.println(treeNode.toRule());
        System.out.println(NodeBuilder.build("!!a"));
    }


//    public static void main(String[] args) {
//        Tree tree = new Tree();
//        ANode a1 = new ANode("a1");
//        ANode a2 = new ANode("a2");
//        CNode c1 = new CNode("c1");
//        CNode c2 = new CNode("c2");
//        tree.setNext(a1);
//        a1.setNext(c1);
//        c1.setNext(c2);
//        c2.setNext(a2);
//        JNode j1 = new JNode();
//        j1.setRule(LNode.and().addRule(new RNode("r1")).addRule(new RNode("r2")));
//        a2.setNext(j1);
//
//        ANode a3 = new ANode("a3");
//        j1.setNext(a3);
//
//        DNode d1 = new DNode();
//        a3.setNext(d1);
//        CNode c3 = new CNode("c3");
//        ANode a4 = new ANode("a4");
//        c3.setNext(a4);
//        d1.addBranch(c3);
//        JNode j2 = new JNode();
//        j2.setRule(LNode.or().addRule(new RNode("r3")).addRule(new RNode("r4")));
//        d1.addBranch(j2);
//        d1.setAction(new ANode("a5"));
//        System.out.println(JsonUtils.toJson(a1));
//
//        System.out.println(tree.toRule());
//    }
}
