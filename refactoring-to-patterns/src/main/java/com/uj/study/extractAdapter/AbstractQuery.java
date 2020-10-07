package com.uj.study.extractAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午7:28
 * @description： SD: super database
 * @modified By：
 * @version:
 */
public abstract class AbstractQuery implements Query {
    private SDQuery sdQuery; // this is needed for SD versions 5.1 & 5.2

    protected abstract SDQuery createQuery();         // a Factory Method [DP]

    @Override
    public void doQuery() throws QueryException {     // a Template Method [DP]
        if (sdQuery != null)
            sdQuery.clearResultSet();
        sdQuery = createQuery();                    // call to the Factory Method
        executeQuery();
    }

    private void executeQuery() {
        //...
    }
}
