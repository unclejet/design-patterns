package com.uj.study.replaceImplicitTreewithComposite;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/1 上午7:04
 * @description：
 * @modified By：
 * @version:
 */
public class OrdersWriter {
    private Orders orders;

    public OrdersWriter(Orders orders) {
        this.orders = orders;
    }

    public String getContents() {
        StringBuffer xml = new StringBuffer();
        writeOrderTo(xml);
        return xml.toString();
    }


    private void writeOrderTo(StringBuffer xml) {
        TagNode ordersTag = new TagNode("orders");
        for (int i = 0; i < orders.getOrderCount(); i++) {
            Order order = orders.getOrder(i);
            TagNode orderTag = new TagNode("order");
            orderTag.addAttribute("id", order.getOrderId());
            writeProductsTo(orderTag, order);
            ordersTag.add(orderTag);
        }
        xml.append(ordersTag.toString());
    }

    private void writeProductsTo(TagNode orderTag, Order order) {
        for (int j=0; j < order.getProductCount(); j++) {
            Product product = order.getProduct(j);
            TagNode productTag = new TagNode("product");
            productTag.addAttribute("id", product.getId());
            productTag.addAttribute("color", colorFor(product));
            if (product.getSize() != ProductSize.NOT_APPLICABLE)
                productTag.addAttribute("size", sizeFor(product));

            writePriceTo(productTag, product);
            productTag.addValue(product.getName());
            orderTag.add(productTag);
        }
    }

    private String sizeFor(Product product) {
        return product.getSize();
    }

    private String colorFor(Product product) {
        return product.getColor();
    }


    private void writePriceTo(TagNode productTag, Product product) {
        TagNode priceTag = new TagNode("price");
        priceTag.addAttribute("currency", currencyFor(product));
        priceTag.addValue(priceFor(product));

        productTag.add(priceTag);
    }

    private String priceFor(Product product) {
        return String.valueOf(product.getPrice());
    }

    private String currencyFor(Product product) {
        return product.getPriceCurrency();
    }
}
