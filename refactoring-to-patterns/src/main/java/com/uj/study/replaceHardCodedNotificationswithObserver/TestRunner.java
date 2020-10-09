package com.uj.study.replaceHardCodedNotificationswithObserver;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/10 上午6:47
 * @description：  Observer
 * @modified By：
 * @version:
 */
public class TestRunner extends Frame {
    // TestRunner for AWT
    private TestResult fTestResult;
    private TestSuite testSuite;
//   ...

    protected TestResult createTestResult() {
        return new UITestResult(this);   // hard-coded to UITestResult
    }

//    protected TextTestResult createTestResult() {
//        return new TextTestResult(
//                this);
//    }

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
}
