package com.uj.study.replaceImplicitLanguagewithInterpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/14 上午6:41
 * @description：
 * @modified By：
 * @version:
 */
class ProductFinderTest {
    private ProductFinder finder;

    private Product fireTruck =
            new Product("f1234", "Fire Truck",
                    Color.red, 8.95f, ProductSize.MEDIUM);

    private Product barbieClassic =
            new Product("b7654", "Barbie Classic",
                    Color.yellow, 15.95f, ProductSize.SMALL);

    private Product frisbee =
            new Product("f4321", "Frisbee",
                    Color.pink, 9.99f, ProductSize.LARGE);

    private Product baseball =
            new Product("b2343", "Baseball",
                    Color.white, 8.95f, ProductSize.NOT_APPLICABLE);

    private Product toyConvertible =
            new Product("p1112", "Toy Porsche Convertible",
                    Color.red, 230.00f, ProductSize.NOT_APPLICABLE);

    private ProductRepository createProductRepository() {
        ProductRepository repository = new ProductRepository();
        repository.add(fireTruck);
        repository.add(barbieClassic);
        repository.add(frisbee);
        repository.add(baseball);
        repository.add(toyConvertible);
        return repository;
    }

    @BeforeEach
    void setUp() {
        finder = new ProductFinder(createProductRepository());
    }

    @Test
    public void testFindByColor() {
        List foundProducts = finder.byColor(Color.red);
        assertEquals(2, foundProducts.size(), "found 2 red products");
        assertTrue(foundProducts.contains(fireTruck), "found fireTruck");
        assertTrue(foundProducts.contains(toyConvertible), "found Toy Porsche Convertible");
    }

    @Test
    public void testFindByPrice() {
        List foundProducts = finder.byPrice(8.95f);
        assertEquals(2, foundProducts.size(), "found products that cost $8.95");
        for (Iterator i = foundProducts.iterator(); i.hasNext();) {
            Product p = (Product) i.next();
            assertTrue(p.getPrice() == 8.95f);
        }
    }

    @Test
    public void testFindByColorSizeAndBelowPrice() {
        List foundProducts =
                finder.byColorSizeAndBelowPrice(Color.red, ProductSize.SMALL, 10.00f);
        assertEquals(0, foundProducts.size(), "found no small red products below $10.00");

        foundProducts =
                finder.byColorSizeAndBelowPrice(Color.red, ProductSize.MEDIUM, 10.00f);
        assertEquals(fireTruck, foundProducts.get(0), "found firetruck when looking for cheap medium red toys");
    }

    @Test
    public void testFindBelowPriceAvoidingAColor() {
        List foundProducts =
                finder.belowPriceAvoidingAColor(9.00f, Color.white);
        assertEquals(1, foundProducts.size(), "found 1 non-white product < $9.00");
        assertTrue(foundProducts.contains(fireTruck), "found fireTruck");

        foundProducts = finder.belowPriceAvoidingAColor(9.00f, Color.red);
        assertEquals(1, foundProducts.size(), "found 1 non-red product < $9.00");
        assertTrue(foundProducts.contains(baseball), "found baseball");
    }
}