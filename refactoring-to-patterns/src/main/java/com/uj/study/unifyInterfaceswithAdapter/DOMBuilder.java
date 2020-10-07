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
    private XMLNode root;
    private XMLNode parent;
    private XMLNode current;
    private ArrayDeque<Element> history;

    public void addAttribute(String name, String value) {
        current.addAttribute(name, value);
    }

    public void addChild(String childTagName) {
        XMLNode childNode = new ElementAdapter(document.createElement(childTagName), document);
        current.add(childNode);
        parent = current;
        current = childNode;
        history.push(((ElementAdapter)current).getElement());
    }

    public void addSibling(String siblingTagName) {
        if (current == root)
            throw new RuntimeException(CANNOT_ADD_BESIDE_ROOT);
        XMLNode siblingNode = new ElementAdapter(document.createElement(siblingTagName), document);
        parent.add(siblingNode);
        current = siblingNode;
        history.pop();
        history.push(((ElementAdapter)current).getElement());
    }

    public void addValue(String value) {
        current.addValue(value);
    }

    public void addBelow(String child) {
        Element childNode = document.createElement(child);
        ((ElementAdapter)current).getElement().appendChild(childNode);
        parent = current;
        ((ElementAdapter)current).setElement(childNode);
        history.push(((ElementAdapter)current).getElement());
    }

    public void addBeside(String sibling) {
        if (current == root)
            throw new RuntimeException(CANNOT_ADD_BESIDE_ROOT);
        Element siblingNode = document.createElement(sibling);
        ((ElementAdapter)current).getElement().appendChild(siblingNode);
        ((ElementAdapter)current).setElement(siblingNode);
        history.pop();
        history.push(((ElementAdapter)current).getElement());
    }
}
