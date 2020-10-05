package com.uj.study.replaceOneManyDistinctionswithComposite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/5 上午9:46
 * @description：
 * @modified By：
 * @version:
 */
public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public void add(Product product) {
        products.add(product);
    }

    public List selectBy(ColorSpec spec) {
        List foundProducts = new ArrayList();
        Iterator products = iterator();
        while (products.hasNext()) {
            Product product = (Product)products.next();
            if (spec.isSatisfiedBy(product))
                foundProducts.add(product);
        }
        return foundProducts;
    }

    public List selectBy(List specs) {
        CompositeSpec spec = new CompositeSpec(specs);
        List foundProducts = new ArrayList();
        Iterator products = iterator();
        while (products.hasNext()) {
            Product product = (Product)products.next();
            Iterator specifications = spec.getSpecs().iterator();
            boolean satisfiesAllSpecs = true;
            while (specifications.hasNext()) {
                Spec productSpec = ((Spec)specifications.next());
                satisfiesAllSpecs &= productSpec.isSatisfiedBy(product);
            }
            if (satisfiesAllSpecs)
                foundProducts.add(product);
        }
        return foundProducts;
    }

    public Iterator iterator() {
        return products.iterator();
    }
}
