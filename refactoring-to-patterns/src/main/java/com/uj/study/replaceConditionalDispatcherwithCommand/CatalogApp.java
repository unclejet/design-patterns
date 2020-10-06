package com.uj.study.replaceConditionalDispatcherwithCommand;

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

    public HandlerResponse executeActionAndGetResponse(String actionName, Map parameters) {
        if (actionName.equals(NEW_WORKSHOP))
            new NewWorkshopHandler(this).execute(parameters);
         else if (actionName.equals(ALL_WORKSHOPS))
            return new AllWorkshopsHandler(this).execute(parameters);
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
