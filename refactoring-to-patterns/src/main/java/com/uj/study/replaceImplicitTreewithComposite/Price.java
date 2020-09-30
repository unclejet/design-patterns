package com.uj.study.replaceImplicitTreewithComposite;

import lombok.Getter;
import lombok.Setter;

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
}
