package com.uj.study.moveEmbellishmenttoDecorator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/11 上午10:18
 * @description：
 * @modified By：
 * @version:
 */
public class DecodingNode extends StringNode {
    public DecodingNode(String text) {
        super(text);
    }

    @Override
    public String toPlainTextString() {
        return Translate.decode(super.toPlainTextString());
    }
}
