package com.uj.study.moveCreationKnowledgetoFactory;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/12 上午6:55
 * @description：
 * @modified By：
 * @version:
 */
public class StringNode implements Node {
    public StringNode(StringBuffer textBuffer, int textBegin, int textEnd) {

    }

    public static Node createStringNode(StringBuffer textBuffer, int textBegin, int textEnd, boolean shouldDecode) {
        if (shouldDecode)
            return new DecodingStringNode(
                    new StringNode(textBuffer, textBegin, textEnd)
            );
        return new StringNode(textBuffer, textBegin, textEnd);
    }
}
