package com.uj.study.replaceImplicitLanguagewithInterpreter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/14 上午7:19
 * @description：
 * @modified By：
 * @version:
 */
public class AndSpec extends Spec {
    private Spec augend, addend;

    public AndSpec(Spec augend, Spec addend) {
        this.augend = augend;
        this.addend = addend;
    }

    public Spec getAddend() {
        return addend;
    }

    public Spec getAugend() {
        return augend;
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return getAugend().isSatisfiedBy(product) &&
                getAddend().isSatisfiedBy(product);
    }
}
