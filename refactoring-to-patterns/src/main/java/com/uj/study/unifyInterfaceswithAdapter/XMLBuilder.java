package com.uj.study.unifyInterfaceswithAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午9:30
 * @description：
 * @modified By：
 * @version:
 */
public class XMLBuilder extends AbstractBuilder {
    private TagNode rootNode;
    private TagNode currentNode;

    public void addChild(String childTagName) {
        addTo(currentNode, childTagName);
    }

    public void addSibling(String siblingTagName) {
        addTo(currentNode.getParent(), siblingTagName);
    }

    private void addTo(TagNode parentNode, String tagName) {
        currentNode = new TagNode(tagName);
        parentNode.add(currentNode);
    }

    public void addAttribute(String name, String value) {
        currentNode.addAttribute(name, value);
    }

    public void addValue(String value) {
        currentNode.addValue(value);
    }
}
