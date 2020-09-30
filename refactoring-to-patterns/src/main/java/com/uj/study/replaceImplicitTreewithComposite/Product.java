package com.uj.study.replaceImplicitTreewithComposite;

import lombok.Getter;
import lombok.Setter;

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
    private String color;
    private String name;
    private Price price;

    public double getPrice() {
        return price.getMoney();
    }

    public String getPriceCurrency() {
        return price.getCurrency();
    }
}
