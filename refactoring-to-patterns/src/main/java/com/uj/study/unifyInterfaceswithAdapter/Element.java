package com.uj.study.unifyInterfaceswithAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午9:27
 * @description：
 * @modified By：
 * @version:
 */
public interface Element {
    void setAttribute(String name, String value);

    void appendChild(Element childNode);
}
