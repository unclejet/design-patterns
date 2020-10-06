package com.uj.study.extractAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午7:29
 * @description：
 * @modified By：
 * @version:
 */
public class QueryException extends Exception {
    public static final String LOGIN_FAILED = "login failed";

    public QueryException(String loginFailed, String s, Exception ex) {

    }
}
