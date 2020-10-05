package com.uj.study.replaceOneManyDistinctionswithComposite;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/5 上午10:07
 * @description：
 * @modified By：
 * @version:
 */
public class SizeSpec extends Spec {
    private String size;

    public SizeSpec(String size) {
        this.size = size;
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return size.equals(product.getSize());
    }
}
