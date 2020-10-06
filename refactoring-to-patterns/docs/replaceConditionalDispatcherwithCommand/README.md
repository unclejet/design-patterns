![R2P](./Screenshot from 2020-10-06 08-59-45.png)

The example code we'll look at comes from a system I cowrote to create and organize Industrial Logic's HTML-based catalogs. Ironically, this system made heavy use of the Command pattern from its earliest evolutions. I decided to rewrite the sections of the system that used the Command pattern to not use the Command pattern in order to produce the kind of bloated, Command-thirsty code that I so frequently encounter in the field.

In the altered code, a class named CatalogApp is responsible for dispatching and executing actions and returning responses. It performs this work within one large conditional statement:

public class CatalogApp...
  private HandlerResponse executeActionAndGetResponse(String actionName, Map parameters)...
    if (actionName.equals(NEW_WORKSHOP)) {
      String nextWorkshopID = workshopManager.getNextWorkshopID();
      StringBuffer newWorkshopContents =
        workshopManager.createNewFileFromTemplate(
          nextWorkshopID,
          workshopManager.getWorkshopDir(),
          workshopManager.getWorkshopTemplate()
        );
      workshopManager.addWorkshop(newWorkshopContents);
      parameters.put("id",nextWorkshopID);
      executeActionAndGetResponse(ALL_WORKSHOPS, parameters);
    } else if (actionName.equals(ALL_WORKSHOPS)) {
      XMLBuilder allWorkshopsXml = new XMLBuilder("workshops");
      WorkshopRepository repository =
        workshopManager.getWorkshopRepository();
      Iterator ids = repository.keyIterator();
      while (ids.hasNext()) {
        String id = (String)ids.next();
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
    } ...many more "else if" statements

The complete conditional spans several pages—I'll spare you the details. The first leg of the conditional handles the creation of a new workshop. The second leg, which happens to be called by the first leg, returns XML that contains summary information for all of Industrial Logic's workshops. I'll show how to refactor this code to use the Command pattern.

## step1
I start by working on the first leg of the conditional. I apply Extract Method [F] to produce the execution method getNewWorkshopResponse():

public class CatalogApp...
  private HandlerResponse executeActionAndGetResponse(String actionName, Map parameters)...
    if (actionName.equals(NEW_WORKSHOP)) {
      
getNewWorkshopResponse(parameters);
    } else if (actionName.equals(ALL_WORKSHOPS)) {
      ...
    } ...many more "else if" statements

  
private void getNewWorkshopResponse(Map parameters) throws Exception {
    
String nextWorkshopID = workshopManager.getNextWorkshopID();
    
StringBuffer newWorkshopContents =
      
workshopManager.createNewFileFromTemplate(
        
nextWorkshopID,
        
workshopManager.getWorkshopDir(),
        
workshopManager.getWorkshopTemplate()
      
);
    
workshopManager.addWorkshop(newWorkshopContents);
    
parameters.put("id",nextWorkshopID);
    
executeActionAndGetResponse(ALL_WORKSHOPS, parameters);
  
}


The compiler and test code are happy with the newly extracted method.

## step2
I now go on to extract the next chunk of request-handling code, which deals with listing all workshops in the catalog:

public class CatalogApp...
  private HandlerResponse executeActionAndGetResponse(String actionName, Map parameters)...
    if (actionName.equals(NEW_WORKSHOP)) {
      getNewWorkshopResponse(parameters);
    } else if (actionName.equals(ALL_WORKSHOPS)) {
      
getAllWorkshopsResponse();
    } ...many more "else if" statements

  
public HandlerResponse getAllWorkshopsResponse() {
    
XMLBuilder allWorkshopsXml = new XMLBuilder("workshops");
    
WorkshopRepository repository =
      
workshopManager.getWorkshopRepository();
    
Iterator ids = repository.keyIterator();
    
while (ids.hasNext()) {
      
String id = (String)ids.next();
      
Workshop workshop = repository.getWorkshop(id);
      
allWorkshopsXml.addBelowParent("workshop");
      
allWorkshopsXml.addAttribute("id", workshop.getID());
      
allWorkshopsXml.addAttribute("name", workshop.getName());
      
allWorkshopsXml.addAttribute("status", workshop.getStatus());
      
allWorkshopsXml.addAttribute("duraction",
        
workshop.getDurationAsString());
    
}
    
String formattedXml = getFormattedData(allWorkshopsXml.toString());
    
return new HandlerResponse(
      
new StringBuffer(formattedXml),
      
ALL_WORKSHOPS_STYLESHEET
    
);
  
}


I compile, test, and repeat this step for all remaining chunks of request-handling code.

## step3
Now I begin creating concrete commands. I first produce the NewWorkshopHandler concrete command by applying Extract Class [F] on the execution method getNewWorkshopResponse():



public class NewWorkshopHandler {
  
private CatalogApp catalogApp;

  
public NewWorkshopHandler(CatalogApp catalogApp) {
    
this.catalogApp = catalogApp;
  
}

  
public HandlerResponse getNewWorkshopResponse(Map parameters) throws Exception {
    
String nextWorkshopID = workshopManager().getNextWorkshopID();
    
StringBuffer newWorkshopContents =
      
WorkshopManager().createNewFileFromTemplate(
        
nextWorkshopID,
        
workshopManager().getWorkshopDir(),
        
workshopManager().getWorkshopTemplate()
      
);
    
workshopManager().addWorkshop(newWorkshopContents);
    
parameters.put("id", nextWorkshopID);
    
catalogApp.executeActionAndGetResponse(ALL_WORKSHOPS, parameters);
  
}

  
private WorkshopManager workshopManager() {
    
return catalogApp.getWorkshopManager();
  
}

}


CatalogApp instantiates and calls an instance of NewWorkshopHandler like so:

public class CatalogApp...
  
public HandlerResponse executeActionAndGetResponse(
    String actionName, Map parameters) throws Exception {
    if (actionName.equals(NEW_WORKSHOP)) {
      
return new NewWorkshopHandler(this).getNewWorkshopResponse(parameters);
    } else if (actionName.equals(ALL_WORKSHOPS)) {
      ...
    } ...

The compiler and tests confirm that these changes work fine. Note that I made executeActionAndGetResponse(…) public because it's called from NewWorkshopHandler.

Before I go on, I apply Compose Method (123) on NewWorkshopHandler's execution method:



public class NewWorkshopHandler...
  
public HandlerResponse getNewWorkshopResponse(Map parameters) throws Exception {
    
createNewWorkshop(parameters);
    
return catalogApp.executeActionAndGetResponse(
      
CatalogApp.ALL_WORKSHOPS, parameters);
  
}

  
private void createNewWorkshop(Map parameters) throws Exception {
    
String nextWorkshopID = workshopManager().getNextWorkshopID();
    
workshopManager().addWorkshop(newWorkshopContents(nextWorkshopID));
    
parameters.put("id",nextWorkshopID);
  
}

  
private StringBuffer newWorkshopContents(String nextWorkshopID) throws Exception {
    
StringBuffer newWorkshopContents = workshopManager().createNewFileFromTemplate(
      
nextWorkshopID,
      
workshopManager().getWorkshopDir(),
      
workshopManager().getWorkshopTemplate()
    
);
    
return newWorkshopContents;
  
}


I repeat this step for additional execution methods that ought to be extracted into their own concrete commands and turned into Composed Methods. AllWorkshopsHandler is the next concrete command I extract. Here's how it looks:



public class AllWorkshopsHandler...
  
private CatalogApp catalogApp;
  
private static String ALL_WORKSHOPS_STYLESHEET="allWorkshops.xsl";
  
private PrettyPrinter prettyPrinter = new PrettyPrinter();

  
public AllWorkshopsHandler(CatalogApp catalogApp) {
    
this.catalogApp = catalogApp;
  
}

  
public HandlerResponse getAllWorkshopsResponse() throws Exception {
    
return new HandlerResponse(
      
new StringBuffer(prettyPrint(allWorkshopsData())),
      
ALL_WORKSHOPS_STYLESHEET
    
);
  
}

  
private String allWorkshopsData() ...

  
private String prettyPrint(String buffer) {
    
return prettyPrinter.format(buffer);
  
}


After performing this step for every concrete command, I look for duplicated code across all of the concrete commands. I don't find much duplication, so there is no need to apply Form Template Method (205).