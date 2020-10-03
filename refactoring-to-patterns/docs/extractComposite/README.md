![R2P](./Screenshot from 2020-10-04 06-29-42.png)

In Extract Superclass [F], Martin Fowler explains that if you have two or more classes with similar features, it makes sense to move the common features to a superclass. This refactoring is similar: it addresses the case when the similar feature is a Composite [DP] that would be better off in a superclass.

This refactoring occurred on the open-source HTML Parser (see http://sourceforge.net/projects/htmlparser). When the parser parses a piece of HTML, it identifies and creates objects representing HTML tags and pieces of text. For example, here's some HTML:

<HTML>
   <BODY>
      Hello, and welcome to my Web page! I work for
      <A HREF="http://industriallogic.com">
         <IMG SRC="http://industriallogic.com/images/logo141x145.gif">
      </A>
   </BODY>
</HTML>

Given such HTML, the parser would create objects of the following types:

Tag (for the <BODY> tag)

StringNode (for the String, "Hello, and welcome . . .")

LinkTag (for the <A HREF="…"> tag)

Because the link tag (<A HREF="…">) contains an image tag (<IMG SRC"…">), you might wonder what the parser does with it. The image tag, which the parser treats as an ImageTag, is treated as a child of the LinkTag. When the parser notices that the link tag contains an image tag, it constructs and gives one ImageTag object as a child to the LinkTag object.

Additional tags in the parser, such as FormTag, TitleTag, and others, are also child containers. As I studied some of these classes, it didn't take long to spot duplicate code for storing and handling child nodes. For example, consider the following:

public class LinkTag extends Tag...
   private Vector nodeVector;

   public String toPlainTextString() {
      StringBuffer sb = new StringBuffer();
      Node node;
      for (Enumeration e=linkData();e.hasMoreElements();) {
         node = (Node)e.nextElement();
         sb.append(node.toPlainTextString());
      }
      return sb.toString();
   }

public class FormTag extends Tag...
   protected Vector allNodesVector;

   public String toPlainTextString() {
      StringBuffer stringRepresentation = new StringBuffer();
      Node node;
      for (Enumeration e=getAllNodesVector().elements();e.hasMoreElements();) {
         node = (Node)e.nextElement();
         stringRepresentation.append(node.toPlainTextString());
      }
      return stringRepresentation.toString();
   }

Because FormTag and LinkTag both contain children, they both have a Vector for storing children, though it goes by a different name in each class. Both classes need to support the toPlainTextString() operation, which outputs the non-HTML-formatted text of the tag's children, so both classes contain logic to iterate over their children and produce plain text. Yet the code to do this operation is nearly identical in these classes! In fact, there are several nearly identical methods in the child-container classes, all of which reek of duplication. So follow along as I apply Extract Composite to this code.

## step1
I must first create an abstract class that will become the superclass of the child-container classes. Because the child-container classes, like LinkTag and FormTag, are already subclasses of Tag, I create the following class:



public abstract class CompositeTag extends Tag {
   
public CompositeTag(
      
int tagBegin,
      
int tagEnd,
      
String tagContents,
      
String tagLine) {
      
super(tagBegin, tagEnd, tagContents, tagLine);
   
}

}

## step2
Now I make the child containers subclasses of CompositeTag:

public class LinkTag extends 
CompositeTag

public class FormTag extends 
CompositeTag

// and so on...

Note that for the remainder of this refactoring, I'll show code from only two child containers, LinkTag and FormTag, even though there are others in the code base.

## step3
I look for a purely duplicated method across all child containers and find toPlainTextString(). Because this method has the same name in each child container, I don't have to change its name anywhere. My first step is to pull up the child Vector that stores children. I do this using the LinkTag class:

public abstract class CompositeTag extends Tag...
   
protected Vector nodeVector;  // pulled-up field

public class LinkTag extends CompositeTag...
   

private Vector nodeVector;


I want FormTag to use the same newly pulled-up Vector, nodeVector (yes, it's an awful name, I'll change it soon), so I rename its local child Vector to be nodeVector:

public class FormTag extends CompositeTag...
   

protected Vector allNodesVector;
   
protected Vector nodeVector;
...

Then I delete this local field (because FormTag inherits it):

public class FormTag extends CompositeTag...
   

protected Vector nodeVector;


Now I can rename nodeVector in the composite:

public abstract class CompositeTag extends Tag...
   

protected Vector nodeVector;
   
protected Vector children;


I'm now ready to pull up the toPlainTextString() method to CompositeTag. My first attempt at doing this with an automated refactoring tool fails because the two methods aren't identical in LinkTag and FormTag. The trouble is that LinkTag gets an iterator on its children by means of the linkData() method, while FormTag gets an iterator on its children by means of the getAllNodesVector().elements():

public class LinkTag extends CompositeTag
   public Enumeration linkData() {
      return children.elements();
   }

   public String toPlainTextString()...
      for (Enumeration e=
linkData();e.hasMoreElements();)
         ...

public class FormTag extends CompositeTag...
   public Vector getAllNodesVector() {
      return children;
   }
   public String toPlainTextString()...
      for (Enumeration e=
getAllNodesVector().elements();e.hasMoreElements();)
         ...

To fix this problem, I must create a consistent method for getting access to a CompositeTag's children. I do this by making LinkTag and FormTag implement an identical method, called children(), which I pull up to CompositeTag:

public abstract class CompositeTag extends Tag...
   
public Enumeration children() {
      
return children.elements();
   
}


The automated refactoring in my IDE now lets me easily pull up toPlainTextString() to CompositeTag. I run my tests and everything works fine.