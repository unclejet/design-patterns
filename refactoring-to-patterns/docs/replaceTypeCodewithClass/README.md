![R2P](./Screenshot from 2020-10-08 06-33-00.png)
This example, which was shown in the code sketch at the beginning of this refactoring and mentioned in the Motivation section, deals with handling permission requests to access software systems. We'll begin by looking at relevant parts of the class, SystemPermission:

public class SystemPermission {
   private String state;
   private boolean granted;

   public final static String REQUESTED = "REQUESTED";
   public final static String CLAIMED = "CLAIMED";
   public final static String DENIED = "DENIED";
   public final static String GRANTED = "GRANTED";

   public SystemPermission() {
      state = REQUESTED;
      granted = false;
   }

   public void claimed() {
      if (state.equals(REQUESTED))
         state = CLAIMED;
   }

   public void denied() {
      if (state.equals(CLAIMED))
         state = DENIED;
   }

   public void granted() {
      if (!state.equals(CLAIMED)) return;
      state = GRANTED;
      granted = true;
   }

   public boolean isGranted() {
      return granted;
   }

   public String getState() {
      return state;
   }
}

## step1-2
The type-unsafe field in SystemPermission is called state. It is assigned to and compared against a family of String constants also defined within SystemPermission. The goal is to make state type-safe by making its type be a class rather than a String.

I begin by self-encapsulating state:

public class SystemPermission...
   public SystemPermission() {
      
setState(REQUESTED);
      granted = false;
   }

   public void claimed() {
      if (
getState().equals(REQUESTED))
         
setState(CLAIMED);
   }

   
private void setState(String state) {
      
this.state = state;
   
}

   public String getState() {  // note: this method already existed
      return state;
   }

   // etc.

This is a trivial change, and my compiler and tests are happy with it.

2. I create a new class and call it PermissionState because it will soon represent the state of a SystemPermission instance.

public class PermissionState {
}

## step3
I choose one constant value that the type-unsafe field is assigned to or compared against and I create a constant representation for it in PermissionState. I do this by declaring a public final static in PermissionState that is an instance of PermissionState:

public final class PermissionState {
   
public final static PermissionState REQUESTED = new PermissionState();
}

I repeat this step for each constant in SystemPermission, yielding the following code:



public class PermissionState {
   
public final static PermissionState REQUESTED = new PermissionState();
   
public final static PermissionState CLAIMED = new PermissionState();
   
public final static PermissionState GRANTED = new PermissionState();
   
public final static PermissionState DENIED = new PermissionState();

}


The compiler accepts this new code.

Now I must decide whether I want to prevent clients from extending or instantiating PermissionState in order to ensure that the only instances of it are its own four constants. In this case, I don't need such a rigorous level of type safety, so I don't define a private constructor or use the final keyword for the new class.

## step 4
Next, I create a type-safe field inside SystemPermission, using the PermissionState type. I also create a setter method for it:

public class SystemPermission...
   private String state;
   
private PermissionState permission;

   
private void setState(PermissionState permission) {
      
this.permission = permission;
   
}

## step5
Now I must find all assignment statements to the type-unsafe field, state, and make similar assignment statements to the type-safe field, permission:

public class SystemPermission...
   public SystemPermission() {
      setState(REQUESTED);
      
setState(PermissionState.REQUESTED);
      granted = false;
   }

   public void claimed() {
      if (getState().equals(REQUESTED)) 
{
         setState(CLAIMED);
         
setState(PermissionState.CLAIMED);
      
}
   }

   public void denied() {
      if (getState().equals(CLAIMED)) 
{
         setState(DENIED);
         
setState(PermissionState.DENIED);
      
}
   }

   public void granted() {
      if (!getState().equals(CLAIMED))
         return;
      setState(GRANTED);
      
setState(PermissionState.GRANTED);
      granted = true;
   }

I confirm that the compiler is OK with these changes.

## step6
Next, I want to change the getter method for state to return a value obtained from the type-safe field, permission. Because the getter method for state returns a String, I'll have to make permission capable of returning a String as well. My first step is to modify PermissionState to support a toString() method that returns the name of each constant:

public class PermissionState {
   
private final String name;

   
private PermissionState(String name) {
      
this.name = name;
   
}

   
public String toString() {
      
return name;
   
}

   public final static PermissionState REQUESTED = new PermissionState(
"REQUESTED");
   public final static PermissionState CLAIMED = new PermissionState(
"CLAIMED");
   public final static PermissionState GRANTED = new PermissionState(
"GRANTED");
   public final static PermissionState DENIED = new PermissionState(
"DENIED");
}

I can now update the getter method for state:

public class SystemPermission...
   public String getState() {
      

return state;
      
return permission.toString();
   }

The compiler and tests confirm that everything is still working.

## step7
I can now delete the type-unsafe field, state, SystemPermission calls to its private setter method, and the setter method itself:

public class SystemPermission...
   

private String state;
   private PermissionState permission;
   private boolean granted;

   public SystemPermission() {
      

setState(REQUESTED);
      setState(PermissionState.REQUESTED);
      granted = false;
   }

   public void claimed() {
      if (getState().equals(REQUESTED)) 

{
         

setState(CLAIMED);
         setState(PermissionState.CLAIMED);
      

}
   }

   public void denied() {
      if (getState().equals(CLAIMED)) 

{
         

setState(DENIED);
         setState(PermissionState.DENIED);
      

}
   }

   public void granted() {
      if (!getState().equals(CLAIMED))
         return;
      

setState(GRANTED);
      setState(PermissionState.GRANTED);
      granted = true;
   }

   

private void setState(String state) {
      

this.state = state;
   

}


I test that SystemPermission still works as usual. It does.

## step8
Now I replace all code that references SystemPermission's type-unsafe constants with code that references PermissionState's contant values. For example, SystemPermission's claimed() method still references the "REQUESTED" type-unsafe constant:

public class SystemPermission...
   public void claimed() {
      if (getState().equals(REQUESTED))  // 
equality logic with type-unsafe constant
         setState(PermissionState.CLAIMED);
   }

I update this code as follows:

public class SystemPermission...
   public 
PermissionState getState() {
      return permission

.toString();
   }

  public void claimed() {
     if (getState().equals(
PermissionState.REQUESTED)) {
        setState(PermissionState.CLAIMED);
  }

I make similar changes throughout SystemPermission. In addition, I update all callers on getState() so that they now work exclusively with PermissionState constants. For example, here's a test method that requires updating:

public class TestStates...
   public void testClaimedBy() {
      SystemPermission permission = new SystemPermission();
      permission.claimed();
      assertEquals(SystemPermission.CLAIMED, permission.getState());
   }

I change this code as follows:

public class TestStates...
   public void testClaimedBy() {
      SystemPermission permission = new SystemPermission();
      permission.claimed();
      assertEquals(
PermissionState.CLAIMED, permission.getState());
   }

After making similar changes throughout the code, I compile and test to confirm that the new type-safe equality logic works correctly.

Finally, I can safely delete SystemPermission's type-unsafe constants because they are no longer being used:

public class SystemPermission...
   

public final static String REQUESTED = "REQUESTED";
   

public final static String CLAIMED = "CLAIMED";
   

public final static String DENIED = "DENIED";
   

public final static String GRANTED = "GRANTED";


Now SystemPermission's assignments to its permission field and all equality comparions with its permission field are type-safe.