package com.uj.study.replaceOneManyDistinctionswithComposite;

import java.awt.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/5 上午9:53
 * @description：
 * @modified By：
 * @version:
 */
public class ColorSpec extends Spec {
    private Color color;

    public ColorSpec(Color color) {
        this.color = color;
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return product.getColor().equals(color);
    }
}
