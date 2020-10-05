package com.uj.study.replaceOneManyDistinctionswithComposite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/5 上午10:18
 * @description：
 * @modified By：
 * @version:
 */
public class CompositeSpec extends Spec {
    private List specs = new ArrayList();

    public void add(Spec spec) {
        specs.add(spec);
    }

    public List getSpecs() {
        return Collections.unmodifiableList(specs);
    }

    @Override
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
