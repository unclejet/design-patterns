package com.uj.study.replaceOneManyDistinctionswithComposite;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/1 上午7:11
 * @description：
 * @modified By：
 * @version:
 */
@Setter
@Getter
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

    public String getPriceCurrency() {
        return price.getCurrency();
    }
}
