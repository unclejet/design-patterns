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

## step4
I must now create a command (as defined in the Mechanics section, an interface or abstract class that declares an execution method that every concrete command must implement). At the moment, every concrete command has an execution method with a different name, and the execution methods take a different number of arguments (namely, one or none):

   if (actionName.equals(NEW_WORKSHOP)) {
     return new NewWorkshopHandler(this).
getNewWorkshopResponse(parameters);
   } else if (actionName.equals(ALL_WORKSHOPS)) {
     return new AllWorkshopsHandler(this).
getAllWorkshopsResponse();
   } ...

Making a command will involve deciding on:

A common execution method name

What information to pass to and obtain from each handler

The common execution method name I choose is execute (a name that's often used when implementing the Command pattern, but by no means the only name to use). Now I must decide what information needs to be passed to and/or obtained from a call to execute(). I survey the concrete commands I've created and learn that a good many of them:

Require information contained in a Map called parameters

Return an object of type HandlerResponse

Throw an Exception

This means that my command must include an execution method with the following signature:

public HandlerResponse execute(Map parameters) throws Exception

I create the command by performing two refactorings on NewWorkshopHandler. First, I rename its getNewWorkshopResponse(…) method to execute(…):

public class NewWorkshopHandler...
   public HandlerResponse 
execute(Map parameters) throws Exception

Next, I apply the refactoring Extract Superclass [F] to produce an abstract class called Handler:



public abstract class Handler {
  
protected CatalogApp catalogApp;

  
public Handler(CatalogApp catalogApp) {
    
this.catalogApp = catalogApp;
  
}

}

public class NewWorkshopHandler 
extends Handler...
  public NewWorkshopHandler(CatalogApp catalogApp) {
    
super(catalogApp);
  }

The compiler is happy with the new class, so I move on.

## step5
Now that I have the command (expressed as the abstract Handler class), I'll make every handler implement it. I do this by making them all extend Handler and implement the execute() method. When I'm done, the handlers may now be invoked identically:

   if (actionName.equals(NEW_WORKSHOP)) {
     return new NewWorkshopHandler(this).
execute(parameters);
   } else if (actionName.equals(ALL_WORKSHOPS)) {
     return new AllWorkshopsHandler(this).
execute(parameters);
   } ...

I compile and run the tests to find that everything is working.

