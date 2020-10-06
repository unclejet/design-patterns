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
    private static final String ALL_WORKSHOPS = "all workshops";
    private static final String ALL_WORKSHOPS_STYLESHEET = "all workshops stylesheet";
    private WorkshopManager workshopManager;

    private HandlerResponse executeActionAndGetResponse(String actionName, Map parameters) {
        if (actionName.equals(NEW_WORKSHOP))
            getNewWorkshopResponse(parameters);
         else if (actionName.equals(ALL_WORKSHOPS))
            return getAllWorkshopsResponse();
        ///...many more "else if" statements
        return null;
    }

    private HandlerResponse getAllWorkshopsResponse() {
        XMLBuilder allWorkshopsXml = new XMLBuilder("workshops");
        WorkshopRepository repository =
                workshopManager.getWorkshopRepository();
        Iterator ids = repository.keyIterator();
        while (ids.hasNext()) {
            String id = (String) ids.next();
            Workshop workshop = repository.getWorkshop(id);
            allWorkshopsXml.addBelowParent("workshop");
            allWorkshopsXml.addAttribute("id", workshop.getID());
            allWorkshopsXml.addAttribute("name", workshop.getName());
            allWorkshopsXml.addAttribute("status", workshop.getStatus());
            allWorkshopsXml.addAttribute("duration",
                    workshop.getDurationAsString());
        }
        String formattedXml = getFormattedData(allWorkshopsXml.toString());
        return new HandlerResponse(
                new StringBuffer(formattedXml),
                ALL_WORKSHOPS_STYLESHEET
        );
    }

    private void getNewWorkshopResponse(Map parameters) {
        String nextWorkshopID = workshopManager.getNextWorkshopID();
        StringBuffer newWorkshopContents =
                workshopManager.createNewFileFromTemplate(
                        nextWorkshopID,
                        workshopManager.getWorkshopDir(),
                        workshopManager.getWorkshopTemplate()
                );
        workshopManager.addWorkshop(newWorkshopContents);
        parameters.put("id", nextWorkshopID);
        executeActionAndGetResponse(ALL_WORKSHOPS, parameters);
    }

    private String getFormattedData(String toString) {
        return null;
    }
}
