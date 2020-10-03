package com.uj.study.moveAccumulationtoCollectingParameter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/3 上午10:51
 * @description：
 * @modified By：
 * @version:
 */
class TagNodeTest {
    private static final String SAMPLE_PRICE = "8.95";

    @Test
    public void testSimpleTagWithOneAttributeAndValue() {
        TagNode priceTag = new TagNode("price");
        priceTag.addAttribute("currency", "USD");
        priceTag.addValue(SAMPLE_PRICE);

        String expected =
                "<price currency=" +
                        "'" +
                        "USD" +
                        "'>" +
                        SAMPLE_PRICE +
                        "</price>";
        assertEquals(expected, priceTag.toString());
    }

    @Test
    public void testCompositeTagOneChild() {
        TagNode productTag = new TagNode("product");
        productTag.add(new TagNode("price"));

        String expected =
                "<product>" +
                        "<price>" +
                        "</price>" +
                        "</product>";
        assertEquals(expected, productTag.toString());
    }

    @Test
    public void testAddingChildrenAndGrandchildren() {

        TagNode ordersTag = new TagNode("orders");
        TagNode orderTag = new TagNode("order");
        TagNode productTag = new TagNode("product");
        ordersTag.add(orderTag);
        orderTag.add(productTag);

        String expected =
                "<orders>" +
                        "<order>" +
                        "<product>" +
                        "</product>" +
                        "</order>" +
                        "</orders>";
        assertEquals(expected, ordersTag.toString());

    }
}