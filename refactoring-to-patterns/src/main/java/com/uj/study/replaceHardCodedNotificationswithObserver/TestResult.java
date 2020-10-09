package com.uj.study.replaceHardCodedNotificationswithObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/10 上午6:47
 * @description：    notifier
 * @modified By：
 * @version:
 */
public class TestResult {
    private List fFailures;
    private List fErrors;
    private int fRunTests;
    private boolean fStop;
    protected TestListener fRunner;

    public TestResult(TestListener runner) {
        this();
        fRunner= runner;
    }

    public TestResult() {
        fFailures = new ArrayList(10);
        fErrors = new ArrayList(10);
        fRunTests = 0;
        fStop = false;
    }

    public synchronized void addError(Test test, Throwable t) {
        //fErrors.add(new TestFailure(test, t));
        fRunner.addError(this, test, t);
    }

    public synchronized void addFailure(Test test, Throwable t) {
        //fFailures.add(new TestFailure(test, t));
        fRunner.addFailure(this, test, t);
    }

    public synchronized void endTest(Test test) {
        fRunner.endTest(this, test);
    }

    public synchronized void startTest(Test test) {
        fRunTests++;
        fRunner.startTest(this, test);
    }
}
