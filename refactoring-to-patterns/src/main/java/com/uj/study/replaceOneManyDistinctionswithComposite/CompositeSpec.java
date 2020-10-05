package com.uj.study.replaceOneManyDistinctionswithComposite;

import java.util.Iterator;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/5 上午10:18
 * @description：
 * @modified By：
 * @version:
 */
public class CompositeSpec {
    private List specs;

    public CompositeSpec(List specs) {
        this.specs = specs;
    }


    public List getSpecs() {
        return specs;
    }

    public boolean isSatisfiedBy(Product product) {
        Iterator specifications = getSpecs().iterator();
        boolean satisfiesAllSpecs = true;
        while (specifications.hasNext()) {
            Spec productSpec = ((Spec)specifications.next());
            satisfiesAllSpecs &= productSpec.isSatisfiedBy(product);
        }
        return satisfiesAllSpecs;
    }
}
