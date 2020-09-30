package com.uj.study.replaceImplicitTreewithComposite;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/1 上午7:11
 * @description：
 * @modified By：
 * @version:
 */
@Getter
@Setter
public class Order {
    private String orderId;
    private List<Product> productList = new ArrayList<>();

    public int getProductCount() {
        return productList.size();
    }

    public Product getProduct(int j) {
        return productList.get(j);
    }

    public void addProduct(Product product) {
        productList.add(product);
    }
}
