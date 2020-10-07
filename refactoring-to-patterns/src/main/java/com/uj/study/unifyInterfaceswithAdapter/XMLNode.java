package com.uj.study.unifyInterfaceswithAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午9:34
 * @description：
 * @modified By：
 * @version:
 */
public interface XMLNode {
    void add(XMLNode childNode);

    void addAttribute(String attribute, String value);

    void addValue(String value);
}
