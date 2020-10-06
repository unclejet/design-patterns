package com.uj.study.replaceConditionalDispatcherwithCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/6 上午9:00
 * @description：
 * @modified By：
 * @version:
 */
public class CatalogApp {
    private static final String NEW_WORKSHOP = "new workshop";
    public static final String ALL_WORKSHOPS = "all workshops";

    private WorkshopManager workshopManager;

    private Map handlers;

    public CatalogApp() {
        createHandlers();
    }

    public HandlerResponse executeActionAndGetResponse(String handlerName, Map parameters) {
        Handler handler = lookupHandlerBy(handlerName);
        return handler.execute(parameters);
    }

    public void createHandlers() {
        handlers = new HashMap();
        handlers.put(NEW_WORKSHOP, new NewWorkshopHandler(this));
        handlers.put(ALL_WORKSHOPS, new AllWorkshopsHandler(this));
//...
    }

    private Handler lookupHandlerBy(String handlerName) {
        return (Handler)handlers.get(handlerName);
    }

    public WorkshopManager getWorkshopManager() {
        return workshopManager;
    }
}
