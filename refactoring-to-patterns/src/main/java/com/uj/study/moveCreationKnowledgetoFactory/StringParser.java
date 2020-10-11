package com.uj.study.moveCreationKnowledgetoFactory;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/12 上午6:55
 * @description：
 * @modified By：
 * @version:
 */
public class StringParser {
    private StringBuffer textBuffer;
    private int textBegin;
    private int textEnd;
    private Parser parser;
    public Node find() {
//    ...
        NodeFactory nodeFactory = new NodeFactory();
        return nodeFactory.createStringNode(textBuffer, textBegin, textEnd, parser.shouldDecodeNodes()
        );
    }
}
