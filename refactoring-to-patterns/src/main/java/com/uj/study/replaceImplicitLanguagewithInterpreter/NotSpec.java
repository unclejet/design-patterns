package com.uj.study.replaceImplicitLanguagewithInterpreter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/14 上午7:24
 * @description：
 * @modified By：
 * @version:
 */
public class NotSpec extends Spec {
    private Spec specToNegate;

    public NotSpec(Spec specToNegate) {
        this.specToNegate = specToNegate;
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return !specToNegate.isSatisfiedBy(product);
    }
}
