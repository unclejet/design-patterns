package com.uj.study.replaceConditionalDispatcherwithCommand;

import java.util.Map;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/6 上午9:38
 * @description：
 * @modified By：
 * @version:
 */
public abstract class Handler {
    protected CatalogApp catalogApp;

    public Handler(CatalogApp catalogApp) {
        this.catalogApp = catalogApp;
    }

    public abstract HandlerResponse execute(Map parameters);
}
