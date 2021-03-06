package com.uj.study.replaceHardCodedNotificationswithObserver;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/10 上午6:47
 * @description：  Observer
 * @modified By：
 * @version:
 */
public class TestRunner extends Frame implements TestListener {
    // TestRunner for AWT
    private TestResult fTestResult;
    private TestSuite testSuite;
//   ...

    protected TestResult createTestResult() {
        TestResult testResult = new TestResult();
        testResult.addObserver(this);
        return testResult;
    }

    synchronized public void runSuite() {
//      ...
        fTestResult = createTestResult();
        testSuite.run(fTestResult);
    }

    public void addFailure(TestResult result, Test test, Throwable t) {
//      ...
// display the failure in a graphical AWT window
    }

    public void addError(TestResult testResult, Test test, Throwable t) {
        System.out.println("E");
    }

    public void endTest(TestResult testResult, Test test) {
    }

    public void startTest(TestResult testResult, Test test) {

    }
}
