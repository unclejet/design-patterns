![R2P](./Screenshot from 2020-10-10 06-43-13.png)

The code sketch at the beginning of this refactoring depicts a piece of the design of Kent Beck and Erich Gamma's JUnit Testing Framework [Beck and Gamma]. In JUnit 2.x, the authors defined two TestResult subclasses called UITestResult and TextTestResult, both of which are Collecting Parameters (see Move Accumulation to Collecting Parameter, 313).

TestResult subclasses gather information from test case objects (e.g., whether a test passed or failed) in order to report that information to a TestRunner, a class that displays test results to the screen. The UITestResult class was hard-coded to report information for a Java Abstract Window Toolkit (AWT) TestRunner, while the TextTestResult was hard-coded to report information for a console-based TestRunner. Here's a look at a part of the UITestResult class and the connection to its TestRunner:

class UITestResult extends TestResult {
   
private TestRunner fRunner;
   UITestResult(
TestRunner runner) {
      
fRunner= runner;
   }
   public synchronized void addFailure(Test test, Throwable t) {
      super.addFailure(test, t);
      
fRunner.addFailure(this, test, t);  // notification to TestRunner
   }
   ...
}

package ui;
public class TestRunner extends Frame {   
// TestRunner for AWT
   private TestResult fTestResult;
   ...
   protected TestResult createTestResult() {
      
return new UITestResult(this);   // hard-coded to UITestResult
   }
   synchronized public void runSuite() {
      ...
      
fTestResult = createTestResult();
      testSuite.run(
fTestResult);
   }
   public void addFailure(TestResult result, Test test, Throwable t) {
      ... 
// display the failure in a graphical AWT window
   }
}

This design was perfectly simple and good, for at this stage in JUnit's evolution, if the TestResult/TestRunner notifications had been programmed with the Observer pattern, the design would have been more sophisticated than it needed to be. That circumstance changed when users of JUnit requested the ability for multiple objects to observe a TestResult at runtime. Now the hard-coded relationship between TestRunner instances and TestResult instances wasn't sufficient. To make a TestResult instance capable of supporting many observers, an Observer implementation was necessary.

Would such a change be a refactoring or an enhancement? Making JUnit's TestRunner instances rely on an Observer implementation, rather than being hard-coded to specific TestResult subclasses, would not change their behavior; it would only make them more loosely coupled to TestResult. On the other hand, making a TestResult class hold onto a collection of observers, rather than just one solitary observer, would be new behavior. So an Observer implementation in this example is both a refactoring (i.e., a behavior-preserving transformation) and an enhancement. However, the refactoring is the essential work here, while the enhancement (supporting a collection of observers rather than just one observer) is simply a consequence of an introduction of the Observer pattern.

## step 1
The first step involves ensuring that every notifier implements only notification methods, instead of performing custom behavior on behalf of a receiver. This is true of UITestResult and not true of TextTestResult. Rather than notifying its TestRunner of test results, TextTestResult reports test results directly to the console using Java's System.out.println() method:

public class TextTestResult extends TestResult...
   public synchronized void addError(Test test, Throwable t) {
      super.addError(test, t);
      
System.out.println("E");
   }
   public synchronized void addFailure(Test test, Throwable t) {
      super.addFailure(test, t);
      
System.out.print("F");
   }

By applying Move Method [F], I make TextTestResult contain pure notification methods, while moving its custom behavior to its associated TestRunner:

package textui;
public class TextTestResult extends TestResult...
   
private TestRunner fRunner;
   TextTestResult(
TestRunner runner) {
      
fRunner= runner;
   }

   public synchronized void addError(Test test, Throwable t) {
      super.addError(test, t);
      
fRunner.addError(this, test, t);
   }

   ...

package textui;
public class TestRunner...
   protected TextTestResult createTestResult() {
      return new TextTestResult(
this);
   }

   
// moved method
   
public void addError(TestResult testResult, Test test, Throwable t) {
      
System.out.println("E");
   
}

   ...

TextTestResult now notifies its TestRunner, which reports information to the screen. I compile and test to confirm that the changes work.

## step 2
Now I want to create an observer interface called TestListener. To create that interface, I apply Extract Interface [F] on the TestRunner associated with the TextTestResult. When choosing what methods to include in the new interface, I must know which methods TextTestResult calls on TestRunner. Those methods are highlighted in bold in the following listing:

class TextTestResult extends TestResult...
   public synchronized void addError(Test test, Throwable t) {
      super.addError(test, t);
      
fRunner.addError(this, test, t);
   }

   public synchronized void addFailure(Test test, Throwable t) {
      super.addFailure(test, t);
      
fRunner.addFailure(this, test, t);
   }

   public synchronized void endTest(Test test) {
      super.endTest(test);
      
fRunner.endTest(this, test);
   }

   public synchronized void startTest(Test test) {
      super.startTest(test);
      
fRunner.startTest(this, test);
   }

Given this information, I extract the following interface:



public interface TestListener {
   
public void addError(TestResult testResult, Test test, Throwable t);
   
public void addFailure(TestResult testResult, Test test, Throwable t);
   
public void startTest(TestResult testResult, Test test);

}

public class TestRunner 
implements TestListener...

Now I inspect the other notifier, UITestResult, to see if it calls TestRunner methods that are not on the TestListener interface. It does—it overrides a TestResult method called endTest(…):

package ui;
class UITestResult extends TestResult...
   public synchronized void endTest(Test test) {
      super.endTest(test);
      
fRunner.endTest(this, test);
   }

That leads me to update TestListener with the additional method:

public interface TestListener...
   
public void endTest(TestResult testResult, Test test);


I compile to confirm that everything works fine. However, it doesn't work because the TestRunner for TextTestResult implements the TestListener interface and does not declare the method endTest(…). No problem; I simply add that method to the TestRunner to make everything run:

public class TestRunner implements TestListener...
   
public void endTest(TestResult testResult, Test test) {
   
}

## step 3
Now I must make UITestResult's associated TestRunner implement TestListener and also make both TextTestResult and UITestResult communicate with their TestRunner instances using the TestListener interface. Here are a few of the changes:

public class TestRunner extends Frame 
implements TestListener...

class UITestResult extends TestResult...
   protected 
TestListener fRunner;

   UITestResult(
TestListener runner) {
      fRunner= runner;
   }

public class TextTestResult extends TestResult...
   protected 
TestListener fRunner;

   TextTestResult(
TestListener runner) {
      fRunner= runner;
   }

I compile and test to confirm that these changes work.

## step4
Now I apply Pull Up Method [F] on every notification method in TextTestResult and UITestResult. This step is tricky because the methods I'll be pulling up already exist on TestResult, the superclass of TextTestResult and UITestResult. To do this correctly, I need to merge code from the TestResult subclasses into TestResult. This yields the following changes:

public class TestResult...
   
protected TestListener fRunner;

   
public TestResult(TestListener runner) {
      
this();
      
fRunner= runner;
   
}

   public TestResult() {
      fFailures= new Vector(10);
      fErrors= new Vector(10);
      fRunTests= 0;
      fStop= false;
   }

   public synchronized void addError(Test test, Throwable t) {
      fErrors.addElement(new TestFailure(test, t));
      
fRunner.addError(this, test, t);
   }

   public synchronized void addFailure(Test test, Throwable t) {
      fFailures.addElement(new TestFailure(test, t));
      
fRunner.addFailure(this, test, t);
   }

   public synchronized void endTest(Test test) {
      
fRunner.endTest(this, test);
   }

   public synchronized void startTest(Test test) {
      fRunTests++;
      
fRunner.startTest(this, test);
   }

package ui;
class UITestResult extends TestResult {
}

package textui;
class TextTestResult extends TestResult {
}

These changes pass the compiler with no problems.

## step 5
I can now update the TestRunner instances to work directly with TestResult. For example, here is a change I make to textui.TestRunner:

package textui;
public class TestRunner implements TestListener...
   protected 
TestResult createTestResult() {
      return 
new TestResult(this);
   }

   protected void doRun(Test suite, boolean wait)...
      
TestResult result= createTestResult();

I make a similar change for ui.TestRunner. Finally, I delete both TextTestResult and UITestResult. I compile and test. The compile is fine, yet the tests fail miserably!

I do some exploring and a little debugging. I discover that my changes to TestResult can cause a null pointer exception when the fRunner field isn't initialized. That circumstance occurs only when TestResult's original constructor is called because it doesn't initialize fRunner. I correct this problem by insulating all calls to fRunner with the following conditional logic:

public class TestResult...
   public synchronized void addError(Test test, Throwable t) {
      fErrors.addElement(new TestFailure(test, t));
      
if (null != fRunner)
         fRunner.addError(this, test, t);
   }

   public synchronized void addFailure(Test test, Throwable t) {
      fFailures.addElement(new TestFailure(test, t));
      
if (null != fRunner)
         fRunner.addFailure(this, test, t);
   }

   // etc.

The tests now pass and I'm happy again. The two TestRunners are now observers of the subject, TestResult. At this point I can delete both TextTestResult and UITestResult because they are no longer being used.

## step 6
The final step involves updating TestResult so it can hold onto and notify one or many observers. I declare a List of observers like so:

public class TestResult...
   
private List observers = new ArrayList();


Then I supply a method by which observers can add themselves to the observers list:

public class TestResult...
   
public void addObserver(TestListener testListener) {
      
observers.add(testListener);
   
}


Next, I update TestResult's notification methods so they work with the list of observers. Here's one such update:

public class TestResult...
   public synchronized void addError(Test test, Throwable t) {
      fErrors.addElement(new TestFailure(test, t));
      
for (Iterator i = observers.iterator();i.hasNext();) {
            
TestListener observer = (TestListener)i.next();
            
observer.addError(this, test, t);
      
}
   }

Finally, I update the TestRunner instances so they use the new addObserver() method rather than calling a TestResult constructor. Here's the change I make to the textui.TestRunner class:

package textui;
public class TestRunner implements TestListener...
   protected TestResult createTestResult() {
      
TestResult testResult = new TestResult();
      
testResult.addObserver(this);
      
return testResult;
   }

After compiling and testing that these changes work, I can delete the now unused constructor in TestResult:

public class TestResult...
   

public TestResult(TestListener runner) {
      

this();
      

fRunner= runner;
   

}


That completes the refactoring to the Observer pattern. Now, TestResult notifications are no longer hard-coded to specific TestRunner instances, and TestResult can handle one or many observers of its results.
