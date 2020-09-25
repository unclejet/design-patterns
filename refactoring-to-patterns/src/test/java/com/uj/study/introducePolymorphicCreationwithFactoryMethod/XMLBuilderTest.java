package com.uj.study.introducePolymorphicCreationwithFactoryMethod;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/25 上午10:12
 * @description：
 * @modified By：
 * @version:
 */
public class XMLBuilderTest {
    private OutputBuilder builder;

    protected OutputBuilder createBuilder(String rootName) {

        return new XMLBuilder(rootName);

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
        builder = createBuilder("orders");
        builder.addBelow("order");
        try {
            builder.addAbove("customer");
            fail("expecting java.lang.RuntimeException");
        } catch (RuntimeException ignored) {}
    }
}
