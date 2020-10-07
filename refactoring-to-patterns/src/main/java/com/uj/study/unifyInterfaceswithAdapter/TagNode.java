package com.uj.study.unifyInterfaceswithAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午9:30
 * @description：
 * @modified By：
 * @version:
 */
public class TagNode implements XMLNode {
    private String tagName;

    public TagNode(String tagName) {
        this.tagName = tagName;
    }

    public TagNode getParent() {
        return null;
    }

    public void add(TagNode currentNode) {

    }

    @Override
    public void add(XMLNode childNode) {

    }

    public void addAttribute(String name, String value) {

    }

    public void addValue(String value) {

    }
}
