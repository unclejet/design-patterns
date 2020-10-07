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
    private Element root;
    private Element parent;
    private Element current;
    private ArrayDeque<Element> history;

    public void addAttribute(String name, String value) {
        current.setAttribute(name, value);
    }

    public void addBelow(String child) {
        Element childNode = document.createElement(child);
        current.appendChild(childNode);
        parent = current;
        current = childNode;
        history.push(current);
    }

    public void addBeside(String sibling) {
        if (current == root)
            throw new RuntimeException(CANNOT_ADD_BESIDE_ROOT);
        Element siblingNode = document.createElement(sibling);
        parent.appendChild(siblingNode);
        current = siblingNode;
        history.pop();
        history.push(current);
    }

    public void addValue(String value) {
        current.appendChild(document.createTextNode(value));
    }
}
