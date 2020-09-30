package com.uj.study.replaceImplicitTreewithComposite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/1 上午7:10
 * @description：
 * @modified By：
 * @version:
 */
public class Orders {
    private List<Order> orderList = new ArrayList<>();

    public int getOrderCount() {
        return orderList.size();
    }

    public Order getOrder(int i) {
        return orderList.get(i);
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }
}
