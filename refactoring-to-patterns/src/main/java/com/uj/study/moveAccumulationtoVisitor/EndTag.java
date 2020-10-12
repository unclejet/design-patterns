package com.uj.study.moveAccumulationtoVisitor;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/13 上午6:34
 * @description：
 * @modified By：
 * @version:
 */
public class EndTag {
    public String getTagName() {
        return null;
    }

    public void accept(TextExtractor textExtractor) {
        textExtractor.visitEndTag(this);
    }
}
