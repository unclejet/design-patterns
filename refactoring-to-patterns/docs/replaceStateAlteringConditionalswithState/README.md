![R2P](./Screenshot from 2020-10-08 07-07-35.png)


To understand when it makes sense to refactor to the State pattern, it helps to study a class that manages its state without requiring the sophistication of the State pattern. SystemPermission is such a class. It uses simple conditional logic to keep track of the state of a permission request to access a software system. Over the lifetime of a SystemPermission object, an instance variable named state transitions between the states requested, claimed, denied, and granted. Here is a state diagram of the possible transitions:

![R2P](./Screenshot from 2020-10-08 07-08-19.png)

Below is the code for SystemPermission and a fragment of test code to show how the class gets used:

public class SystemPermission...
  private SystemProfile profile;
  private SystemUser requestor;
  private SystemAdmin admin;
  private boolean isGranted;
  private String state;

  public final static String REQUESTED = "REQUESTED";
  public final static String CLAIMED = "CLAIMED";
  public final static String GRANTED = "GRANTED";
  public final static String DENIED = "DENIED";

  public SystemPermission(SystemUser requestor, SystemProfile profile) {
    this.requestor = requestor;
    this.profile = profile;
    state = REQUESTED;
    isGranted = false;
    notifyAdminOfPermissionRequest();
  }

  public void claimedBy(SystemAdmin admin) {
    if (!state.equals(REQUESTED))
      return;
    willBeHandledBy(admin);
    state = CLAIMED;
  }

  public void deniedBy(SystemAdmin admin) {
    if (!state.equals(CLAIMED))
      return;
    if (!this.admin.equals(admin))
      return;
    isGranted = false;
    state = DENIED;
    notifyUserOfPermissionRequestResult();
  }

  public void grantedBy(SystemAdmin admin) {
    if (!state.equals(CLAIMED))
      return;
    if (!this.admin.equals(admin))
      return;
    state = GRANTED;
    isGranted = true;
    notifyUserOfPermissionRequestResult();
  }

public class TestStates extends TestCase...
  private SystemPermission permission;

  public void setUp() {
    permission = new SystemPermission(user, profile);
  }

  public void testGrantedBy() {
    permission.grantedBy(admin);
    assertEquals("requested", permission.REQUESTED, permission.state());
    assertEquals("not granted", false, permission.isGranted());
    permission.claimedBy(admin);
    permission.grantedBy(admin);
    assertEquals("granted", permission.GRANTED, permission.state());
    assertEquals("granted", true, permission.isGranted());
  }

Notice how the instance variable, state, gets assigned to different values as clients call specific SystemPermission methods. Now look at the overall conditional logic in SystemPermission. This logic is responsible for transitioning between states, but the logic isn't very complicated so the code doesn't require the sophistication of the State pattern.

This conditional state-changing logic can quickly become hard to follow as more real-world behavior gets added to the SystemPermission class. For example, I helped design a security system in which users needed to obtain UNIX and/or database permissions before the user could be granted general permission to access a given software system. The state transition logic that requires UNIX permission before general permission may be granted looks like this:

![R2P](./Screenshot from 2020-10-08 07-09-21.png)


Adding support for UNIX permission makes SystemPermission's state-altering conditional logic more complicated than it used to be. Consider the following:

public class SystemPermission...
  public void claimedBy(SystemAdmin admin) {
    if (!state.equals(REQUESTED) && !state.equals(UNIX_REQUESTED))
      return;
    willBeHandledBy(admin);
    if (state.equals(REQUESTED))
      state = CLAIMED;
    else if (state.equals(UNIX_REQUESTED))
      state = UNIX_CLAIMED;
  }

  public void deniedBy(SystemAdmin admin) {
    if (!state.equals(CLAIMED) && !state.equals(UNIX_CLAIMED))
      return;
    if (!this.admin.equals(admin))
      return;
    isGranted = false;
    isUnixPermissionGranted = false;
    state = DENIED;
    notifyUserOfPermissionRequestResult();
  }

  public void grantedBy(SystemAdmin admin) {
    if (!state.equals(CLAIMED) && !state.equals(UNIX_CLAIMED))
      return;
    if (!this.admin.equals(admin))
      return;

    if (profile.isUnixPermissionRequired() && state.equals(UNIX_CLAIMED))
      isUnixPermissionGranted = true;
    else if (profile.isUnixPermissionRequired() &&
      !isUnixPermissionGranted()) {
      state = UNIX_REQUESTED;
      notifyUnixAdminsOfPermissionRequest();
      return;
    }
    state = GRANTED;
    isGranted = true;
    notifyUserOfPermissionRequestResult();
  }

An attempt can be made to simplify this code by applying Extract Method [F]. For example, I could refactor the grantedBy() method like so:

public void grantedBy(SystemAdmin admin) {
  if (
!isInClaimedState())
    return;
  if (!this.admin.equals(admin))
    return;
  if (
isUnixPermissionRequestedAndClaimed())
    isUnixPermissionGranted = true;
  else if (
isUnixPermisionDesiredButNotRequested()) {
    state = UNIX_REQUESTED;
    notifyUnixAdminsOfPermissionRequest();
    return;
  }
  ...

Although that's an improvement, SystemPermission now has lots of state-specific Boolean logic (e.g., methods like isUnixPermissionRequestedAndClaimed()), and the grantedBy() method still isn't simple. It's time to see how I simplify things by refactoring to the State pattern.