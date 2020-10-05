package com.uj.study.replaceOneManyDistinctionswithComposite;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/5 上午10:07
 * @description：
 * @modified By：
 * @version:
 */
public class BelowPriceSpec extends Spec {
    private double price;

    public BelowPriceSpec(double price) {
        this.price = price;
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return product.getPrice() < price;
    }
}
