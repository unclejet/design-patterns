package com.uj.study.replaceImplicitLanguagewithInterpreter;

import com.uj.study.replaceOneManyDistinctionswithComposite.Price;

import java.awt.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/14 上午6:41
 * @description：
 * @modified By：
 * @version:
 */
public class Product {
    private String id;
    private String size;
    private Color color;
    private String name;
    private Price price;

    public Product(String id, String name, Color color, double price, String size) {
        this.id = id;
        this.size = size;
        this.color = color;
        this.name = name;
        this.price = new Price(price);
    }

    public double getPrice() {
        return price.getMoney();
    }

    public Color getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }
}
