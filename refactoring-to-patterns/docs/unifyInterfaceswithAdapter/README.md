![R2P](./Screenshot from 2020-10-07 09-19-15.png)

This example relates to code that builds XML (see Replace Implicit Tree with Composite, 178; Encapsulate Composite with Builder, 96; and Introduce Polymorphic Creation with Factory Method, 88). In this case, there are two builders: XMLBuilder and DOMBuilder. Both extend from AbstractBuilder, which implements the OutputBuilder interface:

![R2P](./Screenshot from 2020-10-07 09-20-44.png)

The code in XMLBuilder and DOMBuilder is largely the same, except that XMLBuilder collaborates with a class called TagNode, while DOMBuilder collaborates with objects that implement the Element interface:

public class DOMBuilder extends AbstractBuilder...
   private Document document;
   private Element root;
   private Element parent;
   private Element current;

   public void addAttribute(String name, String value) {
      current.setAttribute(name, value);
   }

   public void addBelow(String child) {
      Element childNode = document.createElement(child);
      current.appendChild(childNode);
      parent = current;
      current = childNode;
      history.push(current);
   }

   public void addBeside(String sibling) {
      if (current == root)
         throw new RuntimeException(CANNOT_ADD_BESIDE_ROOT);
      Element siblingNode = document.createElement(sibling);
      parent.appendChild(siblingNode);
      current = siblingNode;
      history.pop();
      history.push(current);
   }

   public void addValue(String value) {
      current.appendChild(document.createTextNode(value));
   }

And here's the similar code from XMLBuilder:

public class XMLBuilder extends AbstractBuilder...
  private TagNode rootNode;
  private TagNode currentNode;

  public void addChild(String childTagName) {
     addTo(currentNode, childTagName);
  }

  public void addSibling(String siblingTagName) {
     addTo(currentNode.getParent(), siblingTagName);
  }

  private void addTo(TagNode parentNode, String tagName) {
     currentNode = new TagNode(tagName);
     parentNode.add(currentNode);
  }

  public void addAttribute(String name, String value) {
     currentNode.addAttribute(name, value);
  }

  public void addValue(String value) {
     currentNode.addValue(value);
  }

These methods, and numerous others that I'm not showing in order to conserve space, are nearly the same in DOMBuilder and XMLBuilder, except for the fact that each builder works with either TagNode or Element. The goal of this refactoring is to create a common interface for TagNode and Element so that the duplication in the builder methods can be eliminated.

## step1
My first task is to create a common interface. I base this interface on the TagNode class because its interface is the one I prefer for client code. TagNode has about ten methods, five of which are public. The common interface needs only three of these methods. I apply Extract Interface [F] to obtain the desired result:



public interface XMLNode {
   
public abstract void add(XMLNode childNode);
   
public abstract void addAttribute(String attribute, String value);
   
public abstract void addValue(String value);

}

public class TagNode 
implements XMLNode...
   public void add(
XMLNode childNode) {
      children().add(childNode);
   }
   // etc.

I compile and test to make sure these changes worked.

## step2
Now I begin working on the DOMBuilder class. I want to apply Extract Class [F] to DOMBuilder in order to produce an adapter for Element. This results in the creation of the following class:



public class ElementAdapter {
   
Element element;

   
public ElementAdapter(Element element) {
      
this.element = element;
   
}

   
public Element getElement() {
      
return element;
   
}

}

## step3
Now I update all of the Element fields in DOMBuilder to be of type ElementAdapter and update any code that needs to be updated because of this change:

public class DOMBuilder extends AbstractBuilder...
   private Document document;
   private 
ElementAdapter rootNode;
   private 
ElementAdapter parentNode;
   private 
ElementAdapter currentNode;

   public void addAttribute(String name, String value) {
      currentNode.
getElement().setAttribute(name, value);
   }
   public void addChild(String childTagName) {
      
ElementAdapter childNode =
         
new ElementAdapter(document.createElement(childTagName)
);
      currentNode.
getElement().appendChild(childNode
.getElement());
      parentNode = currentNode;
      currentNode = childNode;
      history.push(currentNode);
   }

   public void addSibling(String siblingTagName) {
      if (currentNode == root)
         throw new RuntimeException(CANNOT_ADD_BESIDE_ROOT);
      
ElementAdapter siblingNode =
         
new ElementAdapter(document.createElement(siblingTagName)
);
      parentNode.
getElement().appendChild(siblingNode
.getElement());
      currentNode = siblingNode;
      history.pop();
      history.push(currentNode);
   }