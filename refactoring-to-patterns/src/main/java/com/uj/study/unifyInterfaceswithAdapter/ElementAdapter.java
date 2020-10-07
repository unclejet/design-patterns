package com.uj.study.unifyInterfaceswithAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午9:36
 * @description：
 * @modified By：
 * @version:
 */
public class ElementAdapter {
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

    void addAttribute(String name, String value) {
        getElement().setAttribute(name, value);
    }

    void add(ElementAdapter child) {
        getElement().appendChild(child.getElement());
    }

    void addValue(String value) {
        getElement().appendChild(document.createTextNode(value));
    }
}
