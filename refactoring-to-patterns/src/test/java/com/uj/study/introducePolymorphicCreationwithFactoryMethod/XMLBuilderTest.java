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
public class XMLBuilderTest extends AbstractBuilderTest {
    protected OutputBuilder createBuilder(String rootName) {
        return new XMLBuilder(rootName);
    }

    @Test
    public void testAddAboveRoot() {
        super.testAddAboveRoot();
    }
}
