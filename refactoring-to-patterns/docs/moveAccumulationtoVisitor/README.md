![r2p](Screenshot%20from%202020-10-13%2006-23-17.png)

A Visitor is a class that performs an operation on an object structure. The classes that a Visitor visits are heterogeneous, which means they hold unique information and provide a specific interface to that information. Visitors can easily interact with heterogeneous classes by means of double-dispatch. This means that each of a set of classes accepts a Visitor instance as a parameter (via an "accept" method: accept(Visitor visitor)) and then calls back on the Visitor, passing itself to its corresponding visit method, as shown in the following diagram.

![r2p](Screenshot%20from%202020-10-13%2006-24-31.png)

Because the first argument passed to a Visitor's visit(…) method is an instance of a specific type, the Visitor can call type-specific methods on the instance without performing type-casting. This makes it possible for Visitors to visit classes in the same hierarchy or different hierarchies.

The job of many real-world Visitors is to accumulate information. The Collecting Parameter pattern is also useful in this role (see Move Accumulation to Collecting Parameter, 313). Like a Visitor, a Collecting Parameter may be passed to multiple objects to accumulate information from them. The key difference lies in the ability to easily accumulate information from heterogeneous classes. While Visitors have no trouble with this task due to double-dispatch, Collecting Parameters don't rely on double-dispatch, which limits their ability to gather diverse information from classes with diverse interfaces.

Now let's get back to the question: When do you really need a Visitor? In general, you need a Visitor when you have numerous algorithms to run on the same heterogeneous object structure and no other solution is as simple or succinct as a Visitor. For example, say you have three domain classes, none of which share a common superclass and all of which feature code for producing different XML representations.

![r2p](Screenshot%20from%202020-10-13%2006-27-22.png)

What's wrong with this design? The main problem is that you have to add a new toXml method to each of these domain classes every time you have a new XML representation. In addition, the toXml methods bloat the domain classes with representation code, which is better kept separate from the domain logic, particularly when you have a lot of it. In the Mechanics section, I refer to the toXml methods as internal accumulation methods because they are internal to the classes used in the accumulation. Refactoring to a Visitor changes the design as shown in the following diagram.

![r2p](Screenshot%20from%202020-10-13%2006-28-10.png)

With this new design, the domain classes may be represented using whatever Visitor is appropriate. Furthermore, the copious representation logic that once crowded the domain classes is now encapsulated in the appropriate Visitor.

Another case when a Visitor is needed is when you have numerous external accumulation methods. Such methods typically use an Iterator [DP] and resort to type-casting heterogeneous objects to access specific information:

public String extractText()...
   while (nodes.hasMoreNodes()) {
      Node node = nodes.nextNode();
      if (node instanceof StringNode) {
         
StringNode stringNode = (StringNode)node;
         results.append(
stringNode.getText());
      } else if (node instanceof LinkTag) {
         
LinkTag linkTag = (LinkTag)node;
         if (isPreTag)
            results.append(
link.getLinkText());
         else
            results.append(
link.getLink());
     } else if ...
   }
   
Type-casting objects to access their specific interfaces is acceptable if it's not done frequently. However, if this activity becomes frequent, it's worth considering a better design. Would a Visitor provide a better solution? Perhaps—unless your heterogeneous classes suffer from the smell Alternative Classes with Different Interfaces [F]. In that case, you could likely refactor the classes to have a common interface, thereby making it possible to accumulate information without type-casting or implementing a Visitor. On the other hand, if you can't make heterogeneous classes look homogeneous by means of a common interface and you have numerous external accumulation methods, you can likely arrive at a better solution by refactoring to a Visitor. The opening code sketch and the Example section show such a case.




It takes a good deal of patience to find a real-world case in which refactoring to a Visitor actually makes sense. I found numerous such cases while refactoring code in an open source, streaming HTML parser (see http://sourceforge.net/projects/htmlparser). The refactoring I'll discuss here occurred on an external accumulation method. To help you understand this refactoring, I need to give a brief overview of how the parser works.

As the parser parses HTML or XML, it recognizes tags and strings. For example, consider this HTML:

<HTML>
   <BODY>
      Hello, and welcome to my Web page! I work for
      <A HREF="http://industriallogic.com">
         <IMG SRC="http://industriallogic.com/images/logo141x145.gif">
      </A>
   </BODY>
</HTML>

The parser recognizes the following objects when parsing this HTML:

Tag (for the <BODY> tag)

StringNode (for the String, "Hello, and welcome . . .")

LinkTag (for the <A HREF="…">…</A> tags)

ImageTag (for the <IMG SRC="…"> tag)

EndTag (for the </BODY> tag)

Users of the parser commonly accumulate information from HTML or XML documents. The TextExTRactor class provides an easy way to accumulate textual data from documents. The heart of this class is a method called extractText():

public class TextExtractor...
   public String extractText() throws ParserException {
      Node node;
      boolean isPreTag = false;
      boolean isScriptTag = false;
      StringBuffer results = new StringBuffer();

      parser.flushScanners();
      parser.registerScanners();

      for (NodeIterator e = parser.elements(); e.hasMoreNodes();) {
         node = e.nextNode();
         if (node instanceof StringNode) {
            if (!isScriptTag) {
               StringNode stringNode = (StringNode) node;
               if (isPreTag)
                  results.append(stringNode.getText());
               else {
                  String text = Translate.decode(stringNode.getText());
                  if (getReplaceNonBreakingSpace())
                     text = text.replace('\a0', ' ');
                  if (getCollapse())
                     collapse(results, text);
                  else
                     results.append(text);
               }
            }
         } else if (node instanceof LinkTag) {
            LinkTag link = (LinkTag) node;
            if (isPreTag)
               results.append(link.getLinkText());
            else
               collapse(results, Translate.decode(link.getLinkText()));
            if (getLinks()) {
               results.append("<");
               results.append(link.getLink());
               results.append(">");
            }
         } else if (node instanceof EndTag) {
            EndTag endTag = (EndTag) node;
            String tagName = endTag.getTagName();
            if (tagName.equalsIgnoreCase("PRE"))
               isPreTag = false;
            else if (tagName.equalsIgnoreCase("SCRIPT"))
               isScriptTag = false;
         } else if (node instanceof Tag) {
            Tag tag = (Tag) node;
            String tagName = tag.getTagName();
            if (tagName.equalsIgnoreCase("PRE"))
               isPreTag = true;
            else if (tagName.equalsIgnoreCase("SCRIPT"))
               isScriptTag = true;
         }
      }
      return (results.toString());
   }

This code iterates all nodes returned by the parser, figures out each node's type (using Java's instanceof operator), and then type-casts and accumulates data from each node with some help from local variables and user-configurable Boolean flags.

In deciding whether or how to refactor this code, I consider the following questions:

Would a Visitor implementation provide a simpler, more succinct solution?

Would a Visitor implementation enable similar refactorings in other areas of the parser or in client code to the parser?

Is there a simpler solution than a Visitor? For example, can I accumulate data from each node by using one common method?

Is the existing code sufficient?

I quickly determine that I cannot accumulate data from the nodes by using one common accumulation method. For instance, the code gathers either all of a LinkTag's text or just its link (i.e., URL) by calling two different methods. I also determine that there is no easy way to avoid all of the instanceof calls and type-casts without moving to a Visitor implementation. Is it worth it? I determine that it is because other areas in the parser and client code could also be improved by using a Visitor.

Before beginning the refactoring, I must decide whether it makes sense for the TextExTRactor class to play the role of Visitor or whether to extract a class from it that will play the Visitor role. In this case, because TextExtractor performs only the single responsibility of text extraction, I decide that it will make a perfectly good Visitor. Having made my choice, I proceed with the refactoring.

## step 1
The accumulation method, extractText(), contains three local variables referenced across multiple legs of a conditional statement. I convert these local variables into TextExtractor fields:

public class TextExtractor...
   
private boolean isPreTag;
   
private boolean isScriptTag;
   
private StringBuffer results;

   public String extractText()...
      

boolean isPreTag = false;
      

boolean isScriptTag = false;
      

StringBuffer results = new StringBuffer();
      ...

I compile and test to confirm that the changes work.

## step 2
Now I apply Extract Method [F] on the first chunk of accumulation code for the StringNode type:

public class TextExtractor...
   public String extractText()...
      ...
      for (NodeIterator e = parser.elements(); e.hasMoreNodes();) {
         node = e.nextNode();
         if (node instanceof StringNode) {
            
accept(node);
         } else if (...


   
private void accept(Node node) {
      
if (!isScriptTag) {
         
StringNode stringNode = (StringNode) node;
         
if (isPreTag)
            
results.append(stringNode.getText());
         
else {
            
String text = Translate.decode(stringNode.getText());
            
if (getReplaceNonBreakingSpace())
               
text = text.replace('\a0', ' ');
            
if (getCollapse())
               
collapse(results, text);
            
else
               
results.append(text);
         
}
      
}
   
}


The accept() method currently type-casts its node argument to a StringNode. I will be creating accept() methods for each of the accumulation sources, so I must customize this one to accept an argument of type StringNode:

public class TextExtractor...
   public String extractText()...
      ...
      for (NodeIterator e = parser.elements(); e.hasMoreNodes();) {
         node = e.nextNode();
         if (node instanceof StringNode) {
            accept(
(StringNode)node);
         } else if (...

   private void accept(
StringNode stringNode)...
      if (!isScriptTag) {
         

StringNode stringNode = (StringNode) node;
         ...

After compiling and testing, I repeat this step for all other accumulation sources. This yields the following code:

public class TextExtractor...
   public String extractText()...

      for (NodeIterator e = parser.elements(); e.hasMoreNodes();) {
         node = e.nextNode();
         if (node instanceof StringNode) {
            
accept((StringNode)node);
         } else if (node instanceof LinkTag) {
            
accept((LinkTag)node);
         } else if (node instanceof EndTag) {
            
accept((EndTag)node);
         } else if (node instanceof Tag) {
            
accept((Tag)node);
         }
      }
      return (results.toString());
   }


## step 3
Now I apply Extract Method [F] on the body of the accept(StringNode stringNode) method to produce a visitStringNode() method:

public class TextExtractor...
   private void accept(StringNode stringNode) {
      
visitStringNode(stringNode);
   }

   
private void visitStringNode(StringNode stringNode) {
      
if (!isScriptTag) {
         
if (isPreTag)
            
results.append(stringNode.getText());
         
else {
            
String text = Translate.decode(stringNode.getText());
            
if (getReplaceNonBreakingSpace())
               
text = text.replace('\a0', ' ');
            
if (getCollapse())
               
collapse(results, text);
            
else
               
results.append(text);
         
}
      
}
   
}


After compiling and testing, I repeat this step for all of the accept() methods, yielding the following:

public class TextExtractor...
   private void accept(Tag tag) {
      
visitTag(tag);
   }
   
private void visitTag(Tag tag)...

   private void accept(EndTag endTag) {
      
visitEndTag(endTag);
   }
   
private void visitEndTag(EndTag endTag)...

   private void accept(LinkTag link) {
      
visitLink(link);
   }
   
private void visitLink(LinkTag link)...

   private void accept(StringNode stringNode) {
      
visitStringNode(stringNode);
   }
   
private void visitStringNode(StringNode stringNode)...

## step4
Next, I apply Move Method [F] to move every accept() method to the accumulation source with which it is associated. For example, the following method:

public class TextExtractor...
   private void accept(StringNode stringNode) {
      visitStringNode(stringNode);
   }

is moved to StringNode:

public class StringNode...
   
public void accept(TextExtractor textExtractor) {
      
textExtractor.visitStringNode(this);
   
}


and adjusted to call StringNode like so:

public class TextExtractor...
   private void accept(StringNode stringNode) {
      
stringNode.accept(this);
   }

This transformation requires modifying TextExtractor so its visitStringNode(…) method is public. Once I compile and test that the new code works, I repeat this step to move the accept() methods for Tag, EndTag, and Link to those classes.

## step 5
Now I can apply Inline Method [F] on every call to accept() within exTRactText():

public class TextExtractor...
   public String extractText()...
      for (NodeIterator e = parser.elements(); e.hasMoreNodes();) {
         node = e.nextNode();
         if (node instanceof StringNode) {
            
((StringNode)node).accept(this);
         } else if (node instanceof LinkTag) {
            
((LinkTag)node).accept(this);
         } else if (node instanceof EndTag) {
            
((EndTag)node).accept(this);
         } else if (node instanceof Tag) {
            
((Tag)node).accept(this);
         }
      }
      return (results.toString());
   }

   

private void accept(Tag tag) {
      

tag.accept(this);
      

}
   

private void accept(EndTag endTag) {
      

endTag.accept(this);
   

}

   

private void accept(LinkTag link) {
      

link.accept(this);
   

}

   

private void accept(StringNode stringNode) {
      

stringNode.accept(this);
   

}


I compile and test to confirm that all is well.