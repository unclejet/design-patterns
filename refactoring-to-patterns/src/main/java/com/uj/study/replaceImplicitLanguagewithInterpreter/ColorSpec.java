package com.uj.study.replaceImplicitLanguagewithInterpreter;

import java.awt.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/14 上午7:09
 * @description：
 * @modified By：
 * @version:
 */
public class ColorSpec extends Spec {
    private Color colorOfProductToFind;

    public ColorSpec(Color colorOfProductToFind) {
        this.colorOfProductToFind = colorOfProductToFind;
    }

    public Color getColorOfProductToFind() {
        return colorOfProductToFind;
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return product.getColor().equals(getColorOfProductToFind());
    }
}
