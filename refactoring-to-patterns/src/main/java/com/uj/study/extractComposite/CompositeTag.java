package com.uj.study.extractComposite;

import java.util.Enumeration;
import java.util.Vector;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/4 上午6:44
 * @description：
 * @modified By：
 * @version:
 */
public class CompositeTag extends Tag {
    protected Vector children;

    public CompositeTag(int tagBegin, int tagEnd, String tagContents, String tagLine) {
        super(tagBegin, tagEnd, tagContents, tagLine);
    }

    public Enumeration children() {
        return children.elements();
    }

    public String toPlainTextString() {
        StringBuffer sb = new StringBuffer();
        Node node;
        for (Enumeration e = children(); e.hasMoreElements();) {
            node = (Node)e.nextElement();
            sb.append(node.toPlainTextString());
        }
        return sb.toString();
    }
}
