package com.uj.study.replaceHardCodedNotificationswithObserver;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/10 上午7:02
 * @description：
 * @modified By：
 * @version:
 */
public interface TestListener {
    void addError(TestResult testResult, Test test, Throwable t);

    void addFailure(TestResult testResult, Test test, Throwable t);

    void startTest(TestResult testResult, Test test);

    void endTest(TestResult testResult, Test test);
}
