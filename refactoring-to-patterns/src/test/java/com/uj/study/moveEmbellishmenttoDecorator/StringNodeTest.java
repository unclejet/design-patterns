package com.uj.study.moveEmbellishmenttoDecorator;

import org.junit.jupiter.api.Test;

import java.util.Dictionary;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/11 上午8:44
 * @description：
 * @modified By：
 * @version:
 */
class StringNodeTest {
    private Parser parser = new Parser();

    @Test
    public void testDecodingAmpersand() throws Exception {
        String ENCODED_WORKSHOP_TITLE = "The Testing &amp; Refactoring Workshop";
        String DECODED_WORKSHOP_TITLE = "The Testing & Refactoring Workshop";

        assertEquals(DECODED_WORKSHOP_TITLE,
                parseToObtainDecodedResult(ENCODED_WORKSHOP_TITLE), "ampersand in string");
    }

    private String parseToObtainDecodedResult(String STRING_TO_DECODE)
            throws ParserException {

        StringBuffer decodedContent = new StringBuffer();
        createParser(STRING_TO_DECODE);

        NodeIterator nodes = parser.elements();
        while (nodes.hasMoreNodes()) {
            Node node = nodes.nextNode();
            if (node instanceof StringNode) {
                StringNode stringNode = (StringNode) node;
                decodedContent.append(
                        Translate.decode(stringNode.toPlainTextString())); // decoding step
            }
            if (node instanceof Tag)
                decodedContent.append(node.toHtml());
        }
        return decodedContent.toString();
    }

    private void createParser(String text) {
        Node stringNode = new StringNode(text);
        parser.addNode(stringNode);
    }

    private void addStringNode(String text) {
        Node stringNode = StringNode.createStringNode(text, parser.isShouldDecodeNodes());
        parser.addNode(stringNode);
    }

    @Test
    public void tdd() {
        String ENCODED_WORKSHOP_TITLE = "The Testing &amp; Refactoring Workshop";
        String DECODED_WORKSHOP_TITLE = "The Testing & Refactoring Workshop";

        parser = Parser.createParser();
        parser.setNodeDecoding(true);  // tell parser to decode StringNodes
        addStringNode(ENCODED_WORKSHOP_TITLE);

        NodeIterator nodes = parser.elements();
        StringBuffer decodedContent = new StringBuffer();
        while (nodes.hasMoreNodes())
            decodedContent.append(nodes.nextNode().toPlainTextString());

        assertEquals(DECODED_WORKSHOP_TITLE, decodedContent.toString(),
                "decoded content");
    }
}