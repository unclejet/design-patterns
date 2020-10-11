![R2P](Screenshot from 2020-10-11 07-18-19.png)

When new features in a system are needed, it's common to add new code to old classes. Such new code often embellishes the core responsibility or primary behavior of an existing class. The trouble with some of these embellishments is that they complicate their host classes with new fields, new methods, and new logic, all of which exists for special-case behavior that needs to be executed only some of the time.

The Decorator pattern [DP] offers a good remedy: place each embellishment in its own class and let that class wrap the type of object it needs to embellish so that clients may wrap the embellishment around objects at runtime, when special-case behavior is needed.

![R2P](Screenshot from 2020-10-11 07-23-58.png)


The open source HTML Parser (http://sourceforge.net/projects/htmlparser) allows programs to see the contents of HTML files as specific HTML objects. When the parser encounters tag data or strings sandwiched between tag data, it translates what it finds into the appropriate HTML objects, like Tag, StringNode, EndTag, ImageTag, and so forth. The parser is frequently used to:

Translate the contents of one HTML file to another

Report information about a piece of HTML

Verify the contents of HTML

The Move Embellishment to Decorator refactoring we'll look at concerns the parser's StringNode class. Instances of this class are created at runtime when the parser finds chunks of text sandwiched between tags. For example, consider this HTML:

<BODY>This text will be recognized as a StringNode</BODY>

Given this line of HTML, the parser creates the following objects at runtime:

Tag (for the <BODY> tag)

StringNode (for the String, "This text will be recognized as a StringNode")

EndTag (for the </BODY> tag)

There are a few ways to examine the contents of HTML objects: you can obtain the object's plain-text representation using toPlainTextString(), and you can obtain the object's HTML representation using toHtml(). In addition, some classes in the parser, including StringNode, implement getText() and setText() methods. Yet a call to a StringNode instance's getText() method returns the same plain-text representation that calls to toPlainTextString() and toHtml() return. So why are there three methods for obtaining the same value? It's a typical story of programmers adding new code to classes based on current needs without refactoring existing code to remove duplication. In this case, it's likely that getText() and toPlainTextString() could be consolidated into one method. In this example, I defer that refactoring work until I learn more about why this consolidation wasn't already performed.

A common embellishment to StringNode involves decoding "numeric or character entity references" found in StringNode instances. Typical character reference decodings include the following:

&amp;

decoded to

&

&divide;

decoded to

÷

&lt;

decoded to

<

&rt;

decoded to

>




The parser's translate class has a method called decode(String dataToDecode) that can decode a comprehensive set of numeric and character entity references. Such decoding is an embellishment often applied to StringNode instances after they've been found by the parser. For example, consider the following test code, which parses a fragment of HTML and then iterates through a collection of Node instances, decoding the nodes that are instances of StringNode:

public void testDecodingAmpersand() throws Exception {
   String ENCODED_WORKSHOP_TITLE =
      "The Testing &amp; Refactoring Workshop";

   String DECODED_WORKSHOP_TITLE =
      "The Testing & Refactoring Workshop";

   assertEquals(
      "ampersand in string",
      DECODED_WORKSHOP_TITLE,
      parseToObtainDecodedResult(ENCODED_WORKSHOP_TITLE));
}

private String parseToObtainDecodedResult(String STRING_TO_DECODE)
   throws ParserException {

   StringBuffer decodedContent = new StringBuffer();
   createParser(STRING_TO_DECODE);

   NodeIterator nodes = parser.elements();
   while (nodes.hasMoreNodes()) {
      Node node = nodes.nextNode();
      if (node instanceof StringNode) {
         StringNode stringNode = (StringNode) node;
         decodedContent.append(
            
Translate.decode(stringNode.toPlainTextString())); // decoding step
      }
      if (node instanceof Tag)
         decodedContent.append(node.toHtml());
   }
   return decodedContent.toString();
}

Decoding character and numeric references in StringNode instances is a feature that clients need only some of the time. Yet these clients always perform the decoding themselves, using the same process of iterating nodes, finding nodes that are StringNode instances, and decoding them. Instead of forcing these clients to perform the same decoding steps over and over again, the work could be consolidated in one place by building this decoding behavior into the parser.

I thought of several ways to go about this refactoring and then settled on a straightforward approach: add the decoding embellishment directly to StringNode and observe how the code looks afterward. I had some doubts about this implementation, but I wanted to see how far I could push it until a better design was needed. So, using test-driven development, I added the decoding embellishment to StringNode. This work involved updating test code, changing the Parser class, changing the StringParser class (which instantiates StringNodes) and changing StringNode.

Here's how I updated the above test to drive the creation of this decoding embellishment:

public void testDecodingAmpersand() throws Exception {
   String ENCODED_WORKSHOP_TITLE =
   "The Testing &amp; Refactoring Workshop";

   String DECODED_WORKSHOP_TITLE =
   "The Testing & Refactoring Workshop";

   StringBuffer decodedContent = new StringBuffer();
   Parser parser = Parser.createParser(ENCODED_WORKSHOP_TITLE);
   
parser.setNodeDecoding(true);  // tell parser to decode StringNodes
   NodeIterator nodes = parser.elements();

   while (nodes.hasMoreNodes())
      decodedContent.append(nodes.nextNode().toPlainTextString());

   assertEquals("decoded content",
      DECODED_WORKSHOP_TITLE,
      decodedContent.toString()
   );
}

True to the nature of test-driven development, this updated code wouldn't even compile until I added the code necessary for parser.setNodeDecoding(true). My first step was to extend the Parser class to include a flag for toggling StringNode decoding on or off:

public class Parser...
   
private boolean shouldDecodeNodes = false;

   
public void setNodeDecoding(boolean shouldDecodeNodes) {
      
this.shouldDecodeNodes = shouldDecodeNodes;
   
}


Next, the StringParser class needed some changes. It contains a method called find(…) that locates, instantiates, and returns StringNode instances during parsing. Here's a fragment of the code:

public class StringParser...
   public Node find(NodeReader reader, String input, int position, boolean balance_quotes) {

     ...
     return new StringNode(textBuffer, textBegin, textEnd);
   }

I also changed this code to support the new decoding option:

public class StringParser...
   public Node find(NodeReader reader, String input, int position, boolean balance_quotes) {
                        ...
      return new StringNode(
         textBuffer, textBegin, textEnd, 
reader.getParser().shouldDecodeNodes());
   }

That code wouldn't compile until I added to the Parser class the shouldDecodeNodes() method and created a new StringNode constructor that would take the boolean value supplied by shouldDecodeNodes():



<div>
[View full width]</div>
public class Parser...
   
public boolean shouldDecodeNodes() {
      
return shouldDecodeNodes;
   
}

public class StringNode extends Node...
   
private boolean shouldDecode = false;

   
public StringNode(StringBuffer textBuffer, int textBegin, int textEnd, boolean

 shouldDecode) {
      
this(textBuffer, textBegin, textEnd);
      
this.shouldDecode = shouldDecode;
   
}


Finally, to complete the implementation and make the test pass, I needed to write decoding logic in StringNode:

public class StringNode...
   public String toPlainTextString() {
      String result = textBuffer.toString();
      
if (shouldDecode)
         
result = Translate.decode(result);
      return result;
   }

My tests were now passing. I observed that the parser's new decoding embellishment didn't unduly bloat the code. Yet once you support one embellishment, it's often easy to find others worth supporting. And sure enough, when I looked over more parser client code, I found that it was common to remove escape characters (like \n for newline, \t for tabs) from StringNode instances. So I decided to give the parser an embellishment to remove escape characters as well. Doing that meant adding another flag to the Parser class (which I called shouldRemoveEscapeCharacters), updating StringParser to call a StringNode constructor that could handle both the decoding option and the new option for removing escape characters, and adding the following new code to StringNode:

public class StringNode...
   
private boolean shouldRemoveEscapeCharacters = false;

   
public StringNode(StringBuffer textBuffer, int textBegin, int textEnd,
                     
boolean shouldDecode, boolean shouldRemoveEscapeCharacters) {
      
this(textBuffer, textBegin, textEnd);
      
this.shouldDecode = shouldDecode;
      
this.shouldRemoveEscapeCharacters = shouldRemoveEscapeCharacters;
   
}

   public String toPlainTextString() {
      String result = textBuffer.toString();
      if (shouldDecode)
         result = Translate.decode(result);
      
if (shouldRemoveEscapeCharacters)
         
result = ParserUtils.removeEscapeCharacters(result);
      return result;
   }

The embellishments for decoding and escape character removal simplified client code to the parser. But I didn't like the number of changes I was forced to make across several parser classes just to support each new embellishment. Making such changes across multiple classes was an indication of the code smell Solution Sprawl (43). This smell resulted from:

Too much initialization logic, that is, code to tell Parser and StringParser to toggle an embellishment on or off and to initialize StringNode instances to use one or more embellishments

Too much embellishment logic, that is, special-case logic in StringNode to support each embellishment

I reasoned that the initialization problem could best be solved by handing the parser a Factory instance that instantiated the appropriately configured StringNode instances at runtime (see Move Creation Knowledge to Factory, 68). I further reasoned that the buildup of embellishment logic could be solved by refactoring to either a Decorator or a Strategy. I decided to revisit the initialization problem later and focus on the Decorator or Strategy refactoring now.

So which pattern would be more useful here? As I explored sibling classes of StringNode (such as RemarkNode, which represents a comment in HTML), I saw that they could also benefit from the decoding and escape character removal behavior now in StringNode. If I refactored that behavior into Strategy classes, then StringNode and its sibling classes would need to be altered to know about the Strategy classes. This would not be necessary with a Decorator implementation because the behavior in Decorators could be transparently wrapped around instances of StringNode and its siblings. That appealed to me; I did not like the idea of changing a lot of code in many classes.

What about performance? I did not give much consideration to performance because I tend to let a profiler lead me to performance problems. I further reasoned that if the Decorator refactoring led to slow performance, it wouldn't require much work to refactor it to a Strategy implementation.

Having decided that a Decorator would be better than a Strategy, I now needed to decide whether a Decorator would really be a good fit for the code in question. As I mentioned at the beginning of the Mechanics section, it's vital to learn whether a class is primitive enough to make decoration viable. Primitive, in this case, means that the class doesn't implement a large number of public methods or declare many fields. I discovered that StringNode is primitive, but its superclass, AbstractNode, is not. The diagram on the following page shows AbstractNode.

![R2P](Screenshot from 2020-10-11 08-30-37.png)

I counted ten public methods on AbstractNode. That's not exactly what I'd call a narrow interface, but it isn't broad either. I decided it's small enough to do this refactoring.

Getting ready to refactor, my goal now is to get the embellishment logic out of StringNode by putting each embellishment into its own StringNode Decorator class. If there is ever a need to support multiple embellishments, it will be possible to configure combinations of StringNode Decorators prior to executing a parse.

The following diagram illustrates where StringNode fits into the Node hierarchy and how its decoding logic looked before it was refactored to use a DecodingNode Decorator.

![R2P](Screenshot from 2020-10-11 08-32-02.png)


Here are the steps for refactoring StringNode's decoding logic to a Decorator.


