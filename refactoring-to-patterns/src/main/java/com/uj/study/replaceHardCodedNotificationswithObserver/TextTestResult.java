package com.uj.study.replaceHardCodedNotificationswithObserver;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/10 上午6:55
 * @description： notifier
 * @modified By：
 * @version:
 */
public class TextTestResult extends TestResult {
    private TestRunner fRunner;

    TextTestResult(TestRunner runner) {
        fRunner= runner;
    }

    public synchronized void addError(Test test, Throwable t) {
        super.addError(test, t);
        fRunner.addError(this, test, t);
    }

    public synchronized void addFailure(Test test, Throwable t) {
        super.addFailure(test, t);
        System.out.print("F");
    }
}
