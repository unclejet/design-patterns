![r2p](Screenshot%20from%202020-10-12%2006-35-37.png)

Sometimes you want to assign a field inside an object to a value provided by another object. If the field is already assigned to a local value, you can extract one-half of the assignment statement to a parameter so that a client can supply the field's value rather than the host object.

I needed this refactoring after performing Replace Inheritance with Delegation [F]. At the end of that refactoring, a delegating class contains a field for an object it delegates to (the delegatee). The delegating class assigns this delegate field to a new instance of the delegate. Yet I needed a client object to supply the delegate's value. Extract Parameter allowed me to simply extract the delegate instantiation code to a parameter value supplied by a client.

This example comes from a step I perform during the refactoring Move Embellishment to Decorator (144). The HTML Parser's DecodingNode class contains a field called delegate that is assigned to a new instance of StringNode inside DecodingNode's constructor:

public class DecodingNode implements Node...
   private Node delegate;

   public DecodingNode(StringBuffer textBuffer, int textBegin, int textEnd) {
      delegate = new StringNode(textBuffer, textBegin, textEnd);
   }

Given this code, I apply this refactoring as follows.

1. Since delegate is already assigned to a value within DecodingNode's contructor, I can move to the next step.

2. I apply Add Parameter [F] and use a default value of new StringNode(textBuffer, textBegin, textEnd). I then alter the assignment statement so that it assigns delegate to the parameter value, newDelegate:

public class DecodingNode implements Node...
   private Node delegate;

   public DecodingNode(StringBuffer textBuffer, int textBegin, int textEnd,
                       
Node newDelegate) {
      delegate = 
newDelegate;
   }

This change involves updating the client, StringNode, to pass in the value for newDelegate:

public class StringNode...
   ...
   return new DecodingNode(
      new StringNode(textBuffer, textBegin, textEnd)
   );

I compile and test to confirm that everything still works just fine.

After completing this refactoring, I will apply Remove Parameter [F] several times, so that the constructor for DecodingNode becomes:

public class DecodingNode implements Node...
   private Node delegate;

   public DecodingNode(

StringBuffer textBuffer, int textBegin, int textEnd,
                       
Node newDelegate) {

      delegate = 
newDelegate;
   }

And that's it for this short and sweet refactoring.