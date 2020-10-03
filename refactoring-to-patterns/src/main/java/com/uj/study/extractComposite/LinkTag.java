package com.uj.study.extractComposite;

import java.util.Enumeration;
import java.util.Vector;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/4 上午6:31
 * @description：
 * @modified By：
 * @version:
 */
public class LinkTag extends CompositeTag {
    private Vector nodeVector;

    public LinkTag(int tagBegin, int tagEnd, String tagContents, String tagLine) {
        super(tagBegin, tagEnd, tagContents, tagLine);
    }

    public String toPlainTextString() {
        StringBuffer sb = new StringBuffer();
        Node node;
        for (Enumeration e = linkData(); e.hasMoreElements();) {
            node = (Node)e.nextElement();
            sb.append(node.toPlainTextString());
        }
        return sb.toString();
    }

    private Enumeration linkData() {
        return null;
    }
}
