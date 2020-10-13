![r2p](Screenshot%20from%202020-10-14%2006-37-57.png)

implementing an Interpreter is only slightly more complicated than implementing a Composite. 
这个就和replaceOneManyDistinctionswithComposite用的同一个素材

The code sketch and the Motivation section already gave you an introduction to this example, which is inspired from an inventory management system. That system's Finder classes (AccountFinder, InvoiceFinder, ProductFinder, and so forth) eventually came to suffer from a Combinatorial Explosion smell (45), which necessitated the refactoring to Specification. It's worth noting that this does not reveal a problem with Finder classes: the point is that a time may come when a refactoring to Specification is justified.

I begin by studying the tests and code for a ProductFinder that is in need of this refactoring. I'll start with the test code. Before any test can run, I need a ProductRepository object that's filled with various Product objects and a ProductFinder object that knows about the ProductRepository:

public class ProductFinderTests extends TestCase...
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

   protected void setUp() {
      finder = new ProductFinder(createProductRepository());
   }

   private ProductRepository createProductRepository() {
      ProductRepository repository = new ProductRepository();
      repository.add(fireTruck);
      repository.add(barbieClassic);
      repository.add(frisbee);
      repository.add(baseball);
      repository.add(toyConvertible);
      return repository;
   }

The "toy" products above work fine for test code. Of course, the production code uses real product objects, which are obtained using object-relational mapping logic.

Now I look at a few simple tests and the implementation code that satisfies them. The testFindByColor() method checks whether the ProductFinder.byColor(…) method correctly finds red toys, while testFindByPrice() checks whether ProductFinder.byPrice(…) correctly finds toys at a given price:

public class ProductFinderTests extends TestCase...
   public void testFindByColor() {
      List foundProducts = finder.byColor(Color.red);
      assertEquals("found 2 red products", 2, foundProducts.size());
      assertTrue("found fireTruck", foundProducts.contains(fireTruck));
      assertTrue(
         "found Toy Porsche Convertible",
         foundProducts.contains(toyConvertible));
   }

   public void testFindByPrice() {
      List foundProducts = finder.byPrice(8.95f);
      assertEquals("found products that cost $8.95", 2, foundProducts.size());
      for (Iterator i = foundProducts.iterator(); i.hasNext();) {
         Product p = (Product) i.next();
         assertTrue(p.getPrice() == 8.95f);
      }
   }

Here's the implementation code that satisfies these tests:

public class ProductFinder...
   private ProductRepository repository;

   public ProductFinder(ProductRepository repository) {
      this.repository = repository;
   }

   public List byColor(Color colorOfProductToFind) {
      List foundProducts = new ArrayList();
      Iterator products = repository.iterator();
      while (products.hasNext()) {
         Product product = (Product) products.next();
         if (product.getColor().equals(colorOfProductToFind))
            foundProducts.add(product);
      }
      return foundProducts;
   }
   public List byPrice(float priceLimit) {
      List foundProducts = new ArrayList();
      Iterator products = repository.iterator();
      while (products.hasNext()) {
         Product product = (Product) products.next();
         if (product.getPrice() == priceLimit)
            foundProducts.add(product);
      }
      return foundProducts;
   }

There's plenty of duplicate code in these two methods. I'll be getting rid of that duplication during this refactoring. Meanwhile, I explore some more tests and code that are involved in the Combinatorial Explosion problem. Below, one test is concerned with finding Product instances by color, size, and below a certain price, while the other test is concerned with finding Product instances by color and above a certain price:

public class ProductFinderTests extends TestCase...
   public void testFindByColorSizeAndBelowPrice() {
      List foundProducts =
         finder.byColorSizeAndBelowPrice(Color.red, ProductSize.SMALL, 10.00f);
      assertEquals(
         "found no small red products below $10.00",
         0,
         foundProducts.size());

      foundProducts =
         finder.byColorSizeAndBelowPrice(Color.red, ProductSize.MEDIUM, 10.00f);
      assertEquals(
         "found firetruck when looking for cheap medium red toys",
         fireTruck,
         foundProducts.get(0));
   }

   public void testFindBelowPriceAvoidingAColor() {
      List foundProducts =
         finder.belowPriceAvoidingAColor(9.00f, Color.white);
      assertEquals(
         "found 1 non-white product < $9.00",
         1,
         foundProducts.size());
      assertTrue("found fireTruck", foundProducts.contains(fireTruck));

      foundProducts = finder.belowPriceAvoidingAColor(9.00f, Color.red);
      assertEquals(
         "found 1 non-red product < $9.00",
         1,
         foundProducts.size());
      assertTrue("found baseball", foundProducts.contains(baseball));
   }

Here's how the implementation code looks for these tests:

public class ProductFinder...
   public List byColorSizeAndBelowPrice(Color color, int size, float price) {
      List foundProducts = new ArrayList();
      Iterator products = repository.iterator();
      while (products.hasNext()) {
         Product product = (Product) products.next();
         if (product.getColor() == color
            && product.getSize() == size
            && product.getPrice() < price)
            foundProducts.add(product);
      }
      return foundProducts;
   }
   public List belowPriceAvoidingAColor(float price, Color color) {
      List foundProducts = new ArrayList();
      Iterator products = repository.iterator();
      while (products.hasNext()) {
         Product product = (Product) products.next();
         if (product.getPrice() < price && product.getColor() != color)
            foundProducts.add(product);
      }
      return foundProducts;
   }

Again, I see plenty of duplicate code because each of the specific finder methods iterates over the same repository and selects just those Product instances that match the specified criteria. I'm now ready to begin the refactoring.