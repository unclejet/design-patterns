package com.uj.study.moveAccumulationtoCollectingParameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/1 上午8:12
 * @description：
 * @modified By：
 * @version:
 */
public class TagNode {
    private String name = "";
    private String value = "";
    private StringBuffer attributes;
    private List children;
    private TagNode parent;

    public TagNode(String name) {
        this.name = name;
        attributes = new StringBuffer("");
    }

    public void addAttribute(String attribute, String value) {
        attributes.append(" ");
        attributes.append(attribute);
        attributes.append("='");
        attributes.append(value);
        attributes.append("'");
    }


    public void addValue(String value) {
        this.value = value;
    }

    private List children() {
        if (children == null)
            children = new ArrayList();
        return children;

    }

    public void add(TagNode child) {
        child.setParent(this);
        children().add(child);
    }

    public String toString() {
        StringBuffer result = new StringBuffer("");
        appendContentsTo(result);
        return result.toString();

    }

    private void appendContentsTo(StringBuffer result) {
        writeOpenTagTo(result);
        writeChildrenTo(result);
        writeValueTo(result);
        writeEndTagTo(result);
    }

    private void writeEndTagTo(StringBuffer result) {
        result.append("</");
        result.append(name);
        result.append(">");
    }

    private void writeValueTo(StringBuffer result) {
        if (!value.equals(""))
            result.append(value);
    }


    private void writeChildrenTo(StringBuffer result) {
        Iterator it = children().iterator();
        while (it.hasNext()) {
            TagNode node = (TagNode)it.next();
            node.appendContentsTo(result);  // now recursive call will work
        }
    }

    private void writeOpenTagTo(StringBuffer result) {
        result.append("<");
        result.append(name);
        result.append(attributes.toString());
        result.append(">");
    }

    public TagNode getParent() {
        return parent;
    }

    private void setParent(TagNode parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }
}
