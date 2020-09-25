package com.uj.study.introducePolymorphicCreationwithFactoryMethod;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/25 上午10:25
 * @description：
 * @modified By：
 * @version:
 */
public abstract class AbstractBuilderTest {
    protected OutputBuilder builder;

    protected abstract OutputBuilder createBuilder(String rootName);

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
        } catch (RuntimeException ignored) {
        }

    }

}
