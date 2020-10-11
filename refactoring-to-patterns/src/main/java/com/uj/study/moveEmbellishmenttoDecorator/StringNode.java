package com.uj.study.moveEmbellishmenttoDecorator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/11 上午8:44
 * @description：
 * @modified By：
 * @version:
 */
public class StringNode extends AbstractNode {
    private String text;
    private boolean shouldRemoveEscapeCharacters = false;

    public StringNode(String text) {
        this.text = text;
    }

    public static Node createStringNode(String text,  boolean shouldDecode) {
        if (shouldDecode)
            return new DecodingNode(new StringNode(text));
        return new StringNode(text);
    }

    /**
     * 用于演示boolean的条件越来越多对代码的影响越来越大
     */
//    private StringNode(StringBuffer textBuffer, int textBegin, int textEnd,
//
//                      boolean shouldDecode, boolean shouldRemoveEscapeCharacters) {
//        this(textBuffer, textBegin, textEnd);
//        setShouldDecode(shouldDecode);
//        this.shouldRemoveEscapeCharacters = shouldRemoveEscapeCharacters;
//    }

//    private StringNode(StringBuffer textBuffer, int textBegin, int textEnd) {
//    }

    public String toPlainTextString() {
        //if I extract RemoveEscapeCharactersNode like DecodingNode, then next 2 lines also can deleted.
        if (shouldRemoveEscapeCharacters)
            return ParserUtils.removeEscapeCharacters(text);
        return text;
    }

    @Override
    public String toHtml() {
        return null;
    }
}
