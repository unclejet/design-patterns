package com.uj.study.replaceHardCodedNotificationswithObserver;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/10 上午6:55
 * @description：
 * @modified By：
 * @version:
 */
public class TextTestResult extends TestResult {
    public synchronized void addError(Test test, Throwable t) {
        super.addError(test, t);
        System.out.println("E");
    }

    public synchronized void addFailure(Test test, Throwable t) {
        super.addFailure(test, t);
        System.out.print("F");
    }
}
