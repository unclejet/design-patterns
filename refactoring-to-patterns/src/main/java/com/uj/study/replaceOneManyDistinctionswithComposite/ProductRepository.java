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

    public List selectBy(Spec spec) {
        List foundProducts = new ArrayList();
        Iterator products = iterator();
        while (products.hasNext()) {
            Product product = (Product)products.next();
            if (spec.isSatisfiedBy(product))
                foundProducts.add(product);
        }
        return foundProducts;
    }

    public Iterator iterator() {
        return products.iterator();
    }
}
