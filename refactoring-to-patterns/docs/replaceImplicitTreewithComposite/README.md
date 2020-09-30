![R2P](./Screenshot from 2020-10-01 07-01-12.png)

## init project
The code that produces the implicit tree in the code sketch at the beginning of this refactoring section comes from a shopping system. In that system, there is an OrdersWriter class, which has a getContents() method. Before proceeding with the refactoring, I first break the large getContents() method into smaller methods by applying Compose Method (123) and Move Accumulation to Collecting Parameter (313):

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
    
xml.append("<orders>");
    
for (int i = 0; i < orders.getOrderCount(); i++) {
      
Order order = orders.getOrder(i);
      
xml.append("<order");
      
xml.append(" id='");
      
xml.append(order.getOrderId());
      
xml.append("'>");
      
writeProductsTo(xml, order);
      
xml.append("</order>");
    
}
    
xml.append("</orders>");
  
}

  
private void writeProductsTo(StringBuffer xml, Order order) {
    
for (int j=0; j < order.getProductCount(); j++) {
      
Product product = order.getProduct(j);
      
xml.append("<product");
      
xml.append(" id='");
      
xml.append(product.getID());
      
xml.append("'");
      
xml.append(" color='");
      
xml.append(colorFor(product));
      
xml.append("'");
      
if (product.getSize() != ProductSize.NOT_APPLICABLE) {
        
xml.append(" size='");
        
xml.append(sizeFor(product));
        
xml.append("'");
      
}
      
xml.append(">");
      
writePriceTo(xml, product);
      
xml.append(product.getName());
      
xml.append("</product>");
    
}
  
}

  
private void writePriceTo(StringBuffer xml, Product product) {
    
xml.append("<price");
    
xml.append(" currency='");
    
xml.append(currencyFor(product));
    
xml.append("'>");
    
xml.append(product.getPrice());
    
xml.append("</price>");
  
}


Now that getContents() has been refactored, it's easier to see additional refactoring possibilities. One reader of this code noticed that the methods writeOrderTo(…), writeProductsTo(…), and writePriceTo(…) all loop through the domain objects Order, Product, and Price in order to extract data from them for use in producing XML. This reader wondered why the code doesn't just ask the domain objects for their XML directly, rather than having to build it externally to the domain objects. In other words, if the Order class had a toXML() method and the Product and Price classes had one as well, obtaining XML for an Order would simply involve making one call to an Order's toXML() method. That call would obtain the XML from the Order, as well as the XML from whatever Product instances were part of the Order and whatever Price was associated with each Product. This approach would take advantage of the existing structure of the domain objects, rather than recreating that structure in methods like writeOrderTo(…), writeProductsTo(…), and writePriceTo(…).

As nice as this idea sounds, it isn't a good design when a system must create many XML representations of the same domain objects. For example, the code we've been looking at comes from a shopping system that requires diverse XML representations for the domain objects:

   <order id='987' totalPrice='14.00'>
     <product id='f1234' price='9.00' quantity='1'>
       Fire Truck
     </product>
     <product id='f4321' price='5.00' quantity='1'>
       Rubber Ball
     </product>
   </order>

   <orderHistory>
     <order date='20041120' totalPrice='14.00'>
       <product id='f1234'>
       <product id='f4321'>
     </order>
   </orderHistory>

   <order id='321'>
     <product id='f1234' color='red' size='medium'>
       <price currency='USD'>
         8.95
       </price>
       Fire Truck
     </product>
   </order>

Producing the above XML would be difficult and awkward using a single toXML() method on each domain object because the XML is so different in each case. Given such a situation, you can either choose to do the XML rendering external to the domain objects (as the writeOrderTo(…), writeProductsTo(…), and writePriceTo(…) methods do), or you can pursue a Visitor solution (see Move Accumulation to Visitor, 320).

For this shopping system, which generates a lot of diverse XML for the same domain objects, refactoring to Visitor makes a lot of sense. However, at the moment, the creation of the XML is still not simple; you have to get the formatting just right and remember to close every tag. I want to simplify this XML generation prior to refactoring to Visitor. Because the Composite pattern can help simplify the XML generation, I proceed with this refactoring.

