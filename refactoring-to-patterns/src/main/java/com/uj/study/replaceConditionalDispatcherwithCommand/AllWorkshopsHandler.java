package com.uj.study.replaceConditionalDispatcherwithCommand;

import java.util.Iterator;
import java.util.Map;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/6 上午9:28
 * @description：
 * @modified By：
 * @version:
 */
public class AllWorkshopsHandler extends Handler {
    private static String ALL_WORKSHOPS_STYLESHEET="allWorkshops.xsl";
    private PrettyPrinter prettyPrinter = new PrettyPrinter();

    public AllWorkshopsHandler(CatalogApp catalogApp) {
        super(catalogApp);
    }

    public HandlerResponse execute(Map parameters) {
        return new HandlerResponse(
                new StringBuffer(prettyPrint(allWorkshopsData())),
                ALL_WORKSHOPS_STYLESHEET);
    }

    private String allWorkshopsData() {
        XMLBuilder allWorkshopsXml = new XMLBuilder("workshops");
        WorkshopRepository repository =
                workshopManager().getWorkshopRepository();
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
        return allWorkshopsXml.toString();
    }

    private String prettyPrint(String buffer) {
        return prettyPrinter.format(buffer);
    }

    private WorkshopManager workshopManager() {
        return catalogApp.getWorkshopManager();
    }
}
