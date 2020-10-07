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
   
## step4
Now I create an adaptee invocation method for each adaptee method called by DOMBuilder. I use Extract Method [F] for this purpose, making sure that each extracted method takes an adaptee as an argument and uses that adaptee in its body:

public class DOMBuilder extends AbstractBuilder...
   public void addAttribute(String name, String value) {
      
addAttribute(currentNode, name, value);
   }

   
private void addAttribute(ElementAdapter current, String name, String value) {
      
currentNode.getElement().setAttribute(name, value);
   
}

   public void addChild(String childTagName) {
      ElementAdapter childNode =
         new ElementAdapter(document.createElement(childTagName));
      
add(currentNode, childNode);
      parentNode = currentNode;
      currentNode = childNode;
      history.push(currentNode);
   }

   
private void add(ElementAdapter parent, ElementAdapter child) {
      
parent.getElement().appendChild(child.getElement());
   
}

   public void addSibling(String siblingTagName) {
      if (currentNode == root)
         throw new RuntimeException(CANNOT_ADD_BESIDE_ROOT);
      ElementAdapter siblingNode =
         new ElementAdapter(document.createElement(siblingTagName));
      
add(parentNode, siblingNode);
      currentNode = siblingNode;
      history.pop();
      history.push(currentNode);
   }

   public void addValue(String value) {
      
addValue(currentNode, value);
   }

   
private void addValue(ElementAdapter current, String value) {
      
currentNode.getElement().appendChild(document.createTextNode(value));
   
}

## step5
 I can now move each adaptee invocation method to ElementAdapter using Move Method [F]. I'd like the moved method to resemble the corresponding methods in the common interface, XMLNode, as much as possible. This is easy to do for every method except addValue(…), which I'll address in a moment. Here are the results after moving the addAttribute(…) and add(…) methods:

public class ElementAdapter {
   Element element;

   public ElementAdapter(Element element) {
      this.element = element;
   }

   public Element getElement() {
      return element;
   }

   
public void addAttribute(String name, String value) {
      
getElement().setAttribute(name, value);
   
}

   
public void add(ElementAdapter child) {
      
getElement().appendChild(child.getElement());
   
}
}

And here are examples of changes in DOMBuilder as a result of the move:

public class DOMBuilder extends AbstractBuilder...
   public void addAttribute(String name, String value) {
      
currentNode.addAttribute(name, value);
   }

   public void addChild(String childTagName) {
      ElementAdapter childNode =
         new ElementAdapter(document.createElement(childTagName));
      
currentNode.add(childNode);
      parentNode = currentNode;
      currentNode = childNode;
      history.push(currentNode);
   }

   // etc.

The addValue(…) method is more tricky to move to ElementAdapter because it relies on a field within ElementAdapter called document:

public class DOMBuilder extends AbstractBuilder...
   
private Document document;

   public void addValue(ElementAdapter current, String value) {
      current.getElement().appendChild(
document.createTextNode(value));
   }

I don't want to pass a field of type Document to the addValue(…) method on ElementAdapter because if I do so, that method will move further away from the target, which is the addValue(…) method on XMLNode:

public interface XMLNode...
   public abstract void addValue(String value);

At this point I decide to pass an instance of Document to ElementAdapter via its constructor:

public class ElementAdapter...
   Element element;
   
Document document;

   public ElementAdapter(Element element, 
Document document) {
      this.element = element;
      
this.document = document;
   }

And I make the necessary changes in DOMBuilder to call this updated constructor. Now I can easily move addValue(…):

public class ElementAdapter...
   
public void addValue(String value) {
      
getElement().appendChild(document.createTextNode(value));
   
}
