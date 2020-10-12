package com.uj.study.moveAccumulationtoVisitor;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/13 上午6:34
 * @description：
 * @modified By：
 * @version:
 */
public class LinkTag {
    public String getLinkText() {
        return null;
    }

    public String getLink() {
        return null;
    }

    public void accept(TextExtractor textExtractor) {
        textExtractor.visitLinkTag(this);
    }
}
