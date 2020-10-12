package com.uj.study.moveAccumulationtoVisitor;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/13 上午6:34
 * @description：
 * @modified By：
 * @version:
 */
public class LinkTag extends Tag {
    public String getLinkText() {
        return null;
    }

    public String getLink() {
        return null;
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
        nodeVisitor.visitLinkTag(this);
    }
}
