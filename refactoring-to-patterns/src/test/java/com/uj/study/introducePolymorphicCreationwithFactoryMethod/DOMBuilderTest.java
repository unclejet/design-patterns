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
class DOMBuilderTest extends AbstractBuilderTest {
    protected OutputBuilder createBuilder(String rootName) {
        return new DOMBuilder(rootName);
    }

    @Test
    public void testAddAboveRoot() {
        super.testAddAboveRoot();
    }

}