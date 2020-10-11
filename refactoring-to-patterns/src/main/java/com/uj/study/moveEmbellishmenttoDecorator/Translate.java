package com.uj.study.moveEmbellishmenttoDecorator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/11 上午8:56
 * @description：
 * @modified By：
 * @version:
 */
public class Translate {
    public static String decode(String toPlainTextString) {
        if (toPlainTextString.contains("&amp;"))
            return toPlainTextString.replaceAll("&amp;", "&");
        return toPlainTextString;
    }
}
