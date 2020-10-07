package com.uj.study.unifyInterfaceswithAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午9:36
 * @description：
 * @modified By：
 * @version:
 */
public class ElementAdapter implements XMLNode {
    private Element element;
    private Document document;

    public ElementAdapter(Element element, Document document) {
        this.element = element;
        this.document = document;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    @Override
    public void add(XMLNode childNode) {
        ElementAdapter childElement = (ElementAdapter)childNode;
        getElement().appendChild(childElement.getElement());
    }

    @Override
    public void addAttribute(String name, String value) {
        getElement().setAttribute(name, value);
    }

    @Override
    public void addValue(String value) {
        getElement().appendChild(document.createTextNode(value));
    }

    void add(ElementAdapter child) {
        getElement().appendChild(child.getElement());
    }
}
