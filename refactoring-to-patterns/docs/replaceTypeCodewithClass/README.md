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