package com.uj.study.replaceImplicitLanguagewithInterpreter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/14 上午7:16
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
        return product.getSize().equals(size);
    }
}
