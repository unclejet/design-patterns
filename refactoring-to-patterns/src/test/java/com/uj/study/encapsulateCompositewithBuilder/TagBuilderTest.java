package com.uj.study.encapsulateCompositewithBuilder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/2 上午6:45
 * @description：
 * @modified By：
 * @version:
 */
class TagBuilderTest {
    @Test
    public void testBuildOneNode() {
        String expectedXml = "<flavors>" +
                "</flavors>";
        String actualXml = new TagBuilder("flavors").toXml();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testBuildOneChild() {
        String expectedXml =
                "<flavors>"+
                        "<flavor>" + "</flavor>" +
                        "</flavors>";

        TagBuilder builder = new TagBuilder("flavors");
        builder.addChild("flavor");
        String actualXml = builder.toXml();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testBuildChildrenOfChildren() {
        String expectedXml =
                "<flavors>"+
                        "<flavor>" +
                        "<requirements>" +
                        "<requirement>" + "</requirement>" +
                        "</requirements>" +
                        "</flavor>" +
                        "</flavors>";
        TagBuilder builder = new TagBuilder("flavors");
        builder.addChild("flavor");
        builder.addChild("requirements");
        builder.addChild("requirement");

        String actualXml = builder.toXml();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testBuildSibling() {
        String expectedXml =
                "<flavors>"+
                        "<flavor1>" + "</flavor1>" +
                        "<flavor2>" + "</flavor2>" +
                        "</flavors>";


        TagBuilder builder = new TagBuilder("flavors");

        builder.addChild("flavor1");
        builder.addSibling("flavor2");

        String actualXml = builder.toXml();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testRepeatingChildrenAndGrandchildren() {
        String expectedXml =
                "<flavors>" +
                        "<flavor>" +
                        "<requirements>" +
                        "<requirement>" + "</requirement>" +
                        "</requirements>" +
                        "</flavor>" +
                        "<flavor>" +
                        "<requirements>" +
                        "<requirement>" + "</requirement>" +
                        "</requirements>" +
                        "</flavor>" +
                        "</flavors>";
        TagBuilder builder = new TagBuilder("flavors");
        for (int i = 0; i < 2; i++) {
            builder.addToParent("flavors", "flavor");
            builder.addChild("requirements");
            builder.addChild("requirement");
        }
        assertEquals(expectedXml, builder.toXml());
    }

    @Test
    public void testParentNameNotFound() {
        TagBuilder builder = new TagBuilder("flavors");
        try {
            for (int i=0; i<2; i++) {
//                should be "flavors" not "favors"
                builder.addToParent("favors", "flavor");
                builder.addChild("requirements");
                builder.addChild("requirement");
            }
            fail("should not allow adding to parent that doesn't exist.");
        } catch (RuntimeException runtimeException) {
            String expectedErrorMessage = "missing parent tag: favors";
            assertEquals(expectedErrorMessage, runtimeException.getMessage());
        }
    }
}