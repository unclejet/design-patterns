package com.uj.study.extractComposite;

import java.util.Enumeration;
import java.util.Vector;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/4 上午6:38
 * @description：
 * @modified By：
 * @version:
 */
public class FormTag extends Tag {
    protected Vector allNodesVector;

    public String toPlainTextString() {
        StringBuffer stringRepresentation = new StringBuffer();
        Node node;
        for (Enumeration e = getAllNodesVector().elements(); e.hasMoreElements();) {
            node = (Node)e.nextElement();
            stringRepresentation.append(node.toPlainTextString());
        }
        return stringRepresentation.toString();
    }

    private Vector<Object> getAllNodesVector() {
        return null;
    }
}
