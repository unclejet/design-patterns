package com.uj.study.replaceHardCodedNotificationswithObserver;

import java.util.ArrayList;
import java.util.Iterator;
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
    private List observers = new ArrayList();

    public TestResult(TestListener runner) {
        this();
    }

    public TestResult() {
        fFailures = new ArrayList(10);
        fErrors = new ArrayList(10);
        fRunTests = 0;
        fStop = false;
    }

    public void addObserver(TestListener testListener) {
        observers.add(testListener);
    }

    public synchronized void addError(Test test, Throwable t) {
        //fErrors.add(new TestFailure(test, t));
        for (Iterator i = observers.iterator(); i.hasNext();) {
            TestListener observer = (TestListener)i.next();
            observer.addError(this, test, t);
        }
    }

    public synchronized void addFailure(Test test, Throwable t) {
        //fFailures.add(new TestFailure(test, t));
        for (Iterator i = observers.iterator(); i.hasNext();) {
            TestListener observer = (TestListener)i.next();
            observer.addFailure(this, test, t);
        }
    }

    public synchronized void endTest(Test test) {
        for (Iterator i = observers.iterator(); i.hasNext();) {
            TestListener observer = (TestListener)i.next();
            observer.endTest(this, test);
        }
    }

    public synchronized void startTest(Test test) {
        fRunTests++;
        for (Iterator i = observers.iterator(); i.hasNext();) {
            TestListener observer = (TestListener)i.next();
            observer.startTest(this, test);
        }
    }
}
