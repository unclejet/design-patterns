package com.uj.study.replaceHardCodedNotificationswithObserver;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/10 上午6:47
 * @description： notifier
 * @modified By：
 * @version:
 */
public class UITestResult extends TestResult {
    private TestRunner fRunner;
    UITestResult(TestRunner runner) {
        fRunner= runner;
    }

    public synchronized void addFailure(Test test, Throwable t) {
        super.addFailure(test, t);
        fRunner.addFailure(this, test, t);  // notification to TestRunner
    }
//   ...
}

