package com.uj.study.replaceConditionalDispatcherwithCommand;

import java.util.Map;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/6 上午9:18
 * @description：
 * @modified By：
 * @version:
 */
public class NewWorkshopHandler extends Handler {

    public NewWorkshopHandler(CatalogApp catalogApp) {
        super(catalogApp);
    }

    public HandlerResponse execute(Map parameters) {
        createNewWorkshop(parameters);
        return catalogApp.executeActionAndGetResponse(CatalogApp.ALL_WORKSHOPS, parameters);
    }

    private StringBuffer newWorkshopContents(String nextWorkshopID) {
        StringBuffer newWorkshopContents = workshopManager().createNewFileFromTemplate(
                nextWorkshopID,
                workshopManager().getWorkshopDir(),
                workshopManager().getWorkshopTemplate());
        return newWorkshopContents;
    }

    private void createNewWorkshop(Map parameters) {
        String nextWorkshopID = workshopManager().getNextWorkshopID();
        workshopManager().addWorkshop(newWorkshopContents(nextWorkshopID));
        parameters.put("id", nextWorkshopID);
    }

    private WorkshopManager workshopManager() {
        return catalogApp.getWorkshopManager();
    }
}
