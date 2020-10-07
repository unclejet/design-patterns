package com.uj.study.extractAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午7:48
 * @description：
 * @modified By：
 * @version:
 */
public class QuerySD52 extends AbstractQuery {
    private SDLoginSession sdLoginSession;
    private String sdConfigFileName;

    public QuerySD52(String sdConfigFileName) {
        super();
        this.sdConfigFileName = sdConfigFileName;
    }

    @Override
    public void login(String server, String user, String password) throws QueryException {
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

    @Override
    protected SDQuery createQuery() {
        return sdLoginSession.createQuery(SDQuery.OPEN_FOR_QUERY);
    }
}
