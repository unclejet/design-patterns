package com.uj.study.replaceImplicitTreewithComposite;

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


    public String toString() {
        String result = "<" + name + attributes + ">" + value + "</" + name + ">";
        return result;
    }
}
