package com.uj.study.introducePolymorphicCreationwithFactoryMethod;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/25 上午10:07
 * @description：
 * @modified By：
 * @version:
 */
class DOMBuilderTest {
    private OutputBuilder builder;

    protected OutputBuilder createBuilder(String rootName) {

        return new DOMBuilder(rootName);

    }

    @Test
    public void testAddAboveRoot() {
        String invalidResult =
                "<orders>" +
                        "<order>" +
                        "</order>" +
                        "</orders>" +
                        "<customer>" +
                        "</customer>";
        builder = createBuilder("orders");  // used to be new XMLBuilder("orders")
        builder.addBelow("order");
        try {
            builder.addAbove("customer");
            fail("expecting java.lang.RuntimeException");
        } catch (RuntimeException ignored) {}
    }

}