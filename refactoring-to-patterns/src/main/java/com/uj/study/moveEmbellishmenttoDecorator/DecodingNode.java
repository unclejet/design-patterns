package com.uj.study.moveEmbellishmenttoDecorator;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/11 上午10:18
 * @description：
 * @modified By：
 * @version:
 */
public class DecodingNode implements Node {
    private Node delegate;

    public DecodingNode(Node newDelegate) {
        delegate = newDelegate;
    }

    @Override
    public String toHtml() {
        return delegate.toHtml();
    }

    public String toPlainTextString() {
        return Translate.decode(delegate.toPlainTextString());
    }

    @Override
    public String getText() {
        return delegate.getText();
    }

    @Override
    public void setText(String text) {
        delegate.setText(text);
    }
}
