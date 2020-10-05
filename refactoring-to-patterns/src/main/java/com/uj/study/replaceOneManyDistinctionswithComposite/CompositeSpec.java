package com.uj.study.replaceOneManyDistinctionswithComposite;

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
}
