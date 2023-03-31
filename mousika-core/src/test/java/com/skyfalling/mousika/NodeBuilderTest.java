package com.skyfalling.mousika;

import com.skyfalling.mousika.eval.node.RuleNode;
import com.skyfalling.mousika.eval.parser.NodeBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.skyfalling.mousika.eval.parser.NodeBuilder.build;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class NodeBuilderTest {

    @ParameterizedTest
    @CsvSource(
            {
                    "!!((a&&b&&c)||(c||d||e)),(a&&b&&c)||(c||d||e)",
                    "c1?101?true:false:null,c1?101?true:false:null",
                    "c1?!101&&!102?true:false:null,c1?(!101&&!102)?true:false:null",
                    "1?((2||3)&&(4||5)?6:7):(8&&9?10&&11:12),1?((2||3)&&(4||5))?6:7:(8&&9)?(10&&11):12",
                    "(!(!(1&&2)||(4&&5))&&((6||7))),!(!(1&&2)||(4&&5))&&(6||7)",
                    "(((1&&2&&3))||((a||b||c))),(1&&2&&3)||(a||b||c)"
            }
    )
    public void testParse(String expr, String expected) {
        RuleNode node = build(expr);
        System.out.println(expr = node.expr());
        assertEquals(expected, expr);
    }

    @Test
    public void testParse() {
        String expr =
                "1025?(1016&&!1017&&!1018&&1019&&1020&&1021&&1022&&1023)?1026:1027:1029?"
                        + "(!1024&&1016&&!1028&&!1018&&1019&&1020&&1021&&1023)?1026:1027:1027";
        RuleNode node = build(expr);
        String actual = node.expr();
        assertEquals(actual, expr);
    }


    @Test
    public void testBuild() {
        assertEquals(NodeBuilder.build("1", "2?a:b", "3?c:d").toString(), "1?2?a:b:3?c:d");
        assertEquals(NodeBuilder.build("1", "2?a:b").toString(), "1?2?a:b");
        System.out.println(NodeBuilder.build("1?2?a:b:c").toString());
    }


}
