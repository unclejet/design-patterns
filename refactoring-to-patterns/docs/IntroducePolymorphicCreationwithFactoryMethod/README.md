![Introduce Polymorphic Creation with Factory Method](./Screenshot from 2020-09-15 07-50-32.png)

## Step 1
1. The similar method I first identify is the test method, testAddAboveRoot(). I exTRact its instantiation logic into an instantiation method like so:

public class DOMBuilderTest extends TestCase...
  
protected OutputBuilder createBuilder(String rootName) {
    
return new DOMBuilder(rootName);
  
}

  public void testAddAboveRoot() {
    String invalidResult =
    "<orders>" +
      "<order>" +
      "</order>" +
    "</orders>" +
    "<customer>" +
    "</customer>";
    builder = 
createBuilder("orders");
    builder.addBelow("order");
    try {
      builder.addAbove("customer");
      fail("expecting java.lang.RuntimeException");
    } catch (RuntimeException ignored) {}
  }

Notice that the return type for the new createBuilder(…) method is an OutputBuilder. I use that return type because the sibling subclass, XMLBuilderTest, will need to define its own createBuilder(…) method (in step 2) and I want the instantiation method's signature to be the same for both classes.

I compile and run my tests to ensure that everything's still working.

## Step2
2. Now I repeat step 1 for all other sibling subclasses, which in this case is just XMLBuilderTest:

public class XMLBuilderTest extends TestCase...
  
private OutputBuilder createBuilder(String rootName) {
    
return new XMLBuilder(rootName);
  
}

  public void testAddAboveRoot() {
    String invalidResult =
    "<orders>" +
      "<order>" +
      "</order>" +
    "</orders>" +
    "<customer>" +
    "</customer>";
    builder = 
createBuilder("orders");
    builder.addBelow("order");
    try {
      builder.addAbove("customer");
      fail("expecting java.lang.RuntimeException");
    } catch (RuntimeException ignored) {}
  }

I compile and test to make sure the tests still work.