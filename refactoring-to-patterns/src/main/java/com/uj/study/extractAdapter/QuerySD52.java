package com.uj.study.extractAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午7:48
 * @description：
 * @modified By：
 * @version:
 */
public class QuerySD52 extends Query {
    private SDLoginSession sdLoginSession;

    public void login(String server, String user, String password,
                      String sdConfigFileName) throws QueryException {
        sdLoginSession = new SDLoginSession(sdConfigFileName, false);
        try {
            sdLoginSession.loginSession(server, user, password);
        } catch (SDLoginFailedException lfe) {
            throw new QueryException(QueryException.LOGIN_FAILED,
                    "Login failure\n" + lfe, lfe);
        } catch (SDSocketInitFailedException ife) {
            throw new QueryException(QueryException.LOGIN_FAILED,
                    "Socket fail\n" + ife, ife);
        } catch (SDNotFoundException nfe) {
            throw new QueryException(QueryException.LOGIN_FAILED,
                    "Not found exception\n" + nfe, nfe);
        }
    }

    public void doQuery() throws QueryException {
        if (sdQuery != null)
            sdQuery.clearResultSet();
        sdQuery = sdLoginSession.createQuery(SDQuery.OPEN_FOR_QUERY);
        executeQuery();
    }
}
