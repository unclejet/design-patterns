package com.uj.study.unifyInterfaceswithAdapter;

import java.util.ArrayDeque;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午9:26
 * @description：
 * @modified By：
 * @version:
 */
public class DOMBuilder extends AbstractBuilder {
    private Document document;
    private ElementAdapter root;
    private ElementAdapter parent;
    private ElementAdapter current;
    private ArrayDeque<Element> history;

    public void addAttribute(String name, String value) {
        current.getElement().setAttribute(name, value);
    }

    public void addChild(String childTagName) {
        ElementAdapter childNode = new ElementAdapter(document.createElement(childTagName));
        current.getElement().appendChild(childNode.getElement());
        parent = current;
        current = childNode;
        history.push(current.getElement());
    }

    public void addSibling(String siblingTagName) {
        if (current == root)
            throw new RuntimeException(CANNOT_ADD_BESIDE_ROOT);
        ElementAdapter siblingNode = new ElementAdapter(document.createElement(siblingTagName));
        parent.getElement().appendChild(siblingNode.getElement());
        current = siblingNode;
        history.pop();
        history.push(current.getElement());
    }

    public void addBelow(String child) {
        Element childNode = document.createElement(child);
        current.getElement().appendChild(childNode);
        parent = current;
        current.setElement(childNode);
        history.push(current.getElement());
    }

    public void addBeside(String sibling) {
        if (current == root)
            throw new RuntimeException(CANNOT_ADD_BESIDE_ROOT);
        Element siblingNode = document.createElement(sibling);
        parent.getElement().appendChild(siblingNode);
        current.setElement(siblingNode);
        history.pop();
        history.push(current.getElement());
    }

    public void addValue(String value) {
        current.getElement().appendChild(document.createTextNode(value));
    }
}
