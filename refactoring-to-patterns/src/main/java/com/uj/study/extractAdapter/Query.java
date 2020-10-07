package com.uj.study.extractAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午7:59
 * @description：
 * @modified By：
 * @version:
 */
public interface Query {
    public void login(String server, String user, String password) throws QueryException;
    public void doQuery() throws QueryException;
}
