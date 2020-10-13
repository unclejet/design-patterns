package com.uj.study.replaceImplicitLanguagewithInterpreter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/14 上午6:41
 * @description：
 * @modified By：
 * @version:
 */
public class ProductFinder {
    private ProductRepository repository;

    public ProductFinder(ProductRepository repository) {
        this.repository = repository;
    }

    public List byColor(Color colorOfProductToFind) {
        ColorSpec spec = new ColorSpec(colorOfProductToFind);
        List foundProducts = new ArrayList();
        Iterator products = repository.iterator();
        while (products.hasNext()) {
            Product product = (Product) products.next();
            if (spec.isSatisfiedBy(product))
                foundProducts.add(product);
        }
        return foundProducts;
    }

    public List byPrice(float priceLimit) {
        List foundProducts = new ArrayList();
        Iterator products = repository.iterator();
        while (products.hasNext()) {
            Product product = (Product) products.next();
            if (product.getPrice() == priceLimit)
                foundProducts.add(product);
        }
        return foundProducts;
    }

    public List byColorSizeAndBelowPrice(Color color, String size, float price) {
        ColorSpec colorSpec = new ColorSpec(color);
        BelowPriceSpec priceSpec = new BelowPriceSpec(price);
        SizeSpec sizeSpec = new SizeSpec(size);
        AndSpec spec1 = new AndSpec(new AndSpec(colorSpec, sizeSpec), priceSpec);
        List foundProducts = new ArrayList();
        Iterator products = repository.iterator();
        while (products.hasNext()) {
            Product product = (Product) products.next();
            if (spec1.isSatisfiedBy(product))
                foundProducts.add(product);
        }
        return foundProducts;
    }

    public List belowPriceAvoidingAColor(float price, Color color) {
        AndSpec spec = new AndSpec(new BelowPriceSpec(price), new NotSpec(new ColorSpec(color)));
        List foundProducts = new ArrayList();
        Iterator products = repository.iterator();
        while (products.hasNext()) {
            Product product = (Product) products.next();
            if (spec.isSatisfiedBy(product))
                foundProducts.add(product);
        }
        return foundProducts;
    }
}
