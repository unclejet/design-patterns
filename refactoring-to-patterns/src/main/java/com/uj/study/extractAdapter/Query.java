package com.uj.study.extractAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午7:28
 * @description： SD: super database
 * @modified By：
 * @version:
 */
public class Query {
    protected SDQuery sdQuery; // this is needed for SD versions 5.1 & 5.2

    // this is a login for SD 5.1
    // NOTE: remove this when we convert all aplications to 5.2
    public void login(String server, String user, String password) throws QueryException {
        //do nothing
    }

    // 5.2 login
    public void login(String server, String user, String password,
                      String sdConfigFileName) throws QueryException {
        // do nothing
    }

    public void doQuery() throws QueryException {
        //do nothing
    }

    protected void executeQuery() {
        //...
    }
}
