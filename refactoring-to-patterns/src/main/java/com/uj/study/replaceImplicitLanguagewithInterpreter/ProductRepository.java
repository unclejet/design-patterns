package com.uj.study.replaceImplicitLanguagewithInterpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/14 上午6:44
 * @description：
 * @modified By：
 * @version:
 */
public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public void add(Product product) {
        products.add(product);
    }

    public Iterator iterator() {
        return products.iterator();
    }
}
