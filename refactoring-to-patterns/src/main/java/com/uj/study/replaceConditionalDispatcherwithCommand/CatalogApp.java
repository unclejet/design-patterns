package com.uj.study.replaceConditionalDispatcherwithCommand;

import java.util.Iterator;
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
    private static final String ALL_WORKSHOPS_STYLESHEET = "all workshops stylesheet";

    private WorkshopManager workshopManager;

    public HandlerResponse executeActionAndGetResponse(String actionName, Map parameters) {
        if (actionName.equals(NEW_WORKSHOP))
            new NewWorkshopHandler(this).getNewWorkshopResponse(parameters);
         else if (actionName.equals(ALL_WORKSHOPS))
            return new AllWorkshopsHandler(this).getAllWorkshopsResponse();
        ///...many more "else if" statements
        return null;
    }

    private String getFormattedData(String toString) {
        return null;
    }

    public WorkshopManager getWorkshopManager() {
        return workshopManager;
    }
}
