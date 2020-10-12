package com.uj.study.moveAccumulationtoVisitor;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/13 上午6:32
 * @description：
 * @modified By：
 * @version:
 */
public class StringNode {
    public String getText() {
        return null;
    }

    public void accept(TextExtractor textExtractor) {
        textExtractor.visitStringNode(this);
    }
}
