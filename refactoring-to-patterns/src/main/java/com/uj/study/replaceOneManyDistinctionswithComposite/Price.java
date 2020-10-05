package com.uj.study.replaceOneManyDistinctionswithComposite;

import lombok.Getter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/1 上午7:48
 * @description：
 * @modified By：
 * @version:
 */
@Getter
public class Price {
    private String currency;
    private double money;

    public Price(String currency, double money) {
        this.currency = currency;
        this.money = money;
    }

    public Price(double money) {
        this.currency = "default";
        this.money = money;
    }
}
