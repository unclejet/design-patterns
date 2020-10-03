![R2P](./Screenshot from 2020-10-03 10-50-31.png)
A Collecting Parameter is an object that you pass to methods in order to collect information from those methods. This pattern is often coupled with Composed Method [Beck, SBPP] (see the refactoring Compose Method, 123).

## step1
In this example, I'll show you how to refactor Composite-based code to use a Collecting Parameter. I'll start with a Composite that can model an XML tree (see Replace Implicit Tree with Composite, 178 for a complete example).

The Composite is modeled with a single class, called TagNode, which has a toString() method. The toString() method recursively walks the nodes in an XML tree and produces a final String representation of what it finds. It does a fair amount of work in 11 lines of code. In the steps presented here, I refactor toString() to make it simpler and easier to understand.

1. The following toString() method recursively accumulates information from every tag in a Composite structure and stores results in a variable called result:

class TagNode...
   public String toString() {
      String result = new String();
      result += "<" + tagName + " " + attributes + ">";
      Iterator it = children.iterator();
      while (it.hasNext()) {
         TagNode node = (TagNode)it.next();
         result += node.toString();
      }
      if (!value.equals(""))
         result += value;
      result += "</" + tagName + ">";
      return result;
   }

I change result's type to be a StringBuffer:



StringBuffer result = new StringBuffer("");


The compiler is happy with this change.

## step2
I identify the first information accumulation step: code that concatenates an XML open tag along with any attributes to the result variable. I apply Extract Method [F] on this code as follows, so that this line:

result += "<" + tagName + " " + attributes + ">";

is extracted to:



private void writeOpenTagTo(StringBuffer result) {
  
result.append("<");
  
result.append(name);
  
result.append(attributes.toString());
  
result.append(">");

}


The original code now looks like this:



StringBuffer result = new StringBuffer("");

writeOpenTagTo(result);
...

I compile and test to confirm that everything is OK.

## step3
Next, I want to continue applying Extract Method [F] on parts of the toString() method. I focus on the code that adds child XML nodes to result. This code contains a recursive step (highlighted in bold):

class TagNode...
   public String toString()...
      Iterator it = children.iterator();
      while (it.hasNext()) {
         TagNode node = (TagNode)it.next();
         result += 
node.toString();
      }
      if (!value.equals(""))
         result += value;
      ...
   }

The recursive step means that the Collecting Parameter needs to be passed to the toString() method. But that's a problem, as the following code shows:

private void writeChildrenTo(StringBuffer result) {
   Iterator it = children.iterator();
   while (it.hasNext()) {
      TagNode node = (TagNode)it.next();
      
node.toString(result); // can't do this because toString() doesn't take arguments.
   }
   ...
}

Because toString() doesn't take a StringBuffer as an argument, I can't simply extract the method. I have to find another solution. I decide to solve the problem using a helper method, which will do the work that toString() used to do but will take a StringBuffer as a Collecting Parameter:

public String toString() {
   
StringBuffer result = new StringBuffer("");
   
appendContentsTo(result);
   
return result.toString();
}


private void appendContentsTo(StringBuffer result) {
   
writeOpenTagTo(result);
   
...

}


Now the recursion that's needed can be handled by the appendContentsTo() method:

private String appendContentsTo(StringBuffer result) {
   writeOpenTagTo(result);
   
writeChildrenTo(result);
   
...
   return result.toString();
}

private void writeChildrenTo(StringBuffer result) {
   Iterator it = children.iterator();
   while (it.hasNext()) {
      TagNode node = (TagNode)it.next();
      
node.appendContentsTo(result);  // now recursive call will work
   }
   if (!value.equals(""))
      
result.append(value);
}

As I stare at the writeChildrenTo() method, I realize that it is handling two steps: adding children recursively and adding a value to a tag, when one exists. To make these two separate steps stand out, I extract the code for handling a value into its own method:



private void writeValueTo(StringBuffer result) {
   
if (!value.equals(""))
      
result.append(value);

}


To finish the refactoring, I extract one more method that writes an XML close tag. Here's how the final code looks:

public class TagNode
...
   public String toString() {
      
StringBuffer result = new StringBuffer("");
      
appendContentsTo(result);
      
return result.toString();
   }

   private void appendContentsTo(StringBuffer result) {
      writeOpenTagTo(result);
      writeChildrenTo(result);
      writeValueTo(result);
      writeEndTagTo(result);
   }

   private void writeOpenTagTo(StringBuffer result) {
      result.append("<");
      result.append(name);
      result.append(attributes.toString());
      result.append(">");
   }

   private void writeChildrenTo(StringBuffer result) {
      Iterator it = children.iterator();
      while (it.hasNext()) {
         TagNode node = (TagNode)it.next();
         node.appendContentsTo(result);
      }
   }

   private void writeValueTo(StringBuffer result) {
      if (!value.equals(""))
         result.append(value);
   }

   private void writeEndTagTo(StringBuffer result) {
      result.append("</");
      result.append(name);
      result.append(">");
   }
}

I compile, run my tests, and everything is good. The toString() method is now very simple, while the appendContentsTo() method is a fine example of a Composed Method (see Compose Method, 123).
