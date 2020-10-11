package com.uj.study.moveEmbellishmenttoDecorator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/11 上午8:55
 * @description：
 * @modified By：
 * @version:
 */
public interface Node {
    String toHtml();

    String toPlainTextString();

    String getText();

    void setText(String text);
}
