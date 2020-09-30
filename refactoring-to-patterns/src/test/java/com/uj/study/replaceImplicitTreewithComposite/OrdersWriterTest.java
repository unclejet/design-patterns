package com.uj.study.replaceImplicitTreewithComposite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/1 上午7:21
 * @description：
 * @modified By：
 * @version:
 */
class OrdersWriterTest {

    @Test
    void aCharacteristicTest() {
        Product product = getProduct();
        Order order = getOrder();
        order.addProduct(product);
        Orders orders = new Orders();
        orders.addOrder(order);
        OrdersWriter ordersWriter = new OrdersWriter(orders);

        String actual = ordersWriter.getContents();

        String expectedResult =
                "<orders>" +
                        "<order id='321'>" +
                        "<product id='f1234' color='red' size='medium'>" +
                        "<price currency='USD'>" +
                        "8.95" +
                        "</price>" +
                        "Fire Truck" +
                        "</product>" +
                        "</order>" +
                        "</orders>";
        assertEquals(expectedResult, actual);
    }

    private Product getProduct() {
        Product product = new Product();
        product.setId("f1234");
        product.setColor("red");
        product.setSize("medium");
        product.setName("Fire Truck");
        Price price = new Price("USD", 8.95);
        product.setPrice(price);
        return product;
    }

    private Order getOrder() {
        Order order = new Order();
        order.setOrderId("321");
        return order;
    }
}