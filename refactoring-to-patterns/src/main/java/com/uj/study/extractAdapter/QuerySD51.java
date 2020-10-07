package com.uj.study.extractAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午7:41
 * @description：
 * @modified By：
 * @version:
 */
public class QuerySD51 extends AbstractQuery {
    private SDLogin sdLogin;
    private SDSession sdSession;

    public QuerySD51() {
        super();
    }

    @Override
    public void login(String server, String user, String password) throws QueryException {
        try {
            sdSession = sdLogin.loginSession(server, user, password);
        } catch (SDLoginFailedException lfe) {
            throw new QueryException(QueryException.LOGIN_FAILED,
                    "Login failure\n" + lfe, lfe);
        } catch (SDSocketInitFailedException ife) {
            throw new QueryException(QueryException.LOGIN_FAILED,
                    "Socket fail\n" + ife, ife);
        }
    }

    @Override
    protected SDQuery createQuery() {
        return sdSession.createQuery(SDQuery.OPEN_FOR_QUERY);
    }
}
