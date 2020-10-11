package com.uj.study.moveEmbellishmenttoDecorator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/11 上午8:44
 * @description：
 * @modified By：
 * @version:
 */
public class StringNode implements Node {
    private String text;
    private boolean shouldDecode = false;
    private boolean shouldRemoveEscapeCharacters = false;

    public StringNode(String text) {
        this.text = text;
    }

    public StringNode(String text,  boolean shouldDecode) {
        this.text = text;
        this.shouldDecode = shouldDecode;
    }

    /**
     * 用于演示boolean的条件越来越多对代码的影响越来越大
     * @param textBuffer
     * @param textBegin
     * @param textEnd
     * @param shouldDecode
     * @param shouldRemoveEscapeCharacters
     */
    public StringNode(StringBuffer textBuffer, int textBegin, int textEnd,

                      boolean shouldDecode, boolean shouldRemoveEscapeCharacters) {
        this(textBuffer, textBegin, textEnd);
        this.shouldDecode = shouldDecode;
        this.shouldRemoveEscapeCharacters = shouldRemoveEscapeCharacters;
    }

    public StringNode(StringBuffer textBuffer, int textBegin, int textEnd) {
    }


    public String toPlainTextString() {
        if (shouldDecode) {
            return Translate.decode(text);
        }
        if (shouldRemoveEscapeCharacters)
            return ParserUtils.removeEscapeCharacters(text);
        return text;
    }

    @Override
    public String toHtml() {
        return null;
    }
}
