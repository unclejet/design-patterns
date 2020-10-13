package com.uj.study.replaceImplicitLanguagewithInterpreter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/14 上午7:13
 * @description：
 * @modified By：
 * @version:
 */
public class BelowPriceSpec extends Spec {
    private double priceThreshold;

    public BelowPriceSpec(double priceThreshold) {
        this.priceThreshold = priceThreshold;
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return product.getPrice() < getPriceThreshold();
    }

    public double getPriceThreshold() {
        return priceThreshold;
    }
}
