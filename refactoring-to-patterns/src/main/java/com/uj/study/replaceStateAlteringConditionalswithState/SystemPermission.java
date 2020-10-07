package com.uj.study.replaceStateAlteringConditionalswithState;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午7:10
 * @description：
 * @modified By：
 * @version:
 */
public class SystemPermission {
    private SystemProfile profile;
    private SystemUser requestor;
    private SystemAdmin admin;
    boolean isGranted;
    private boolean isUnixPermissionGranted;
    //    String state;
    private PermissionState permissionState;

//    public final static String REQUESTED = "REQUESTED";
//    public final static String CLAIMED = "CLAIMED";
//    public final static String GRANTED = "GRANTED";
//    public final static String DENIED = "DENIED";
//    public final static String UNIX_REQUESTED = "UNIX_REQUESTED";
//    public final static String UNIX_CLAIMED = "UNIX_CLAIMED";

    public SystemPermission(SystemUser requestor, SystemProfile profile) {
        this.requestor = requestor;
        this.profile = profile;
        setState(PermissionState.REQUESTED);
        isGranted = false;
        notifyAdminOfPermissionRequest();
    }

    private void notifyAdminOfPermissionRequest() {

    }

    private void willBeHandledBy(SystemAdmin admin) {
        this.admin = admin;
    }

    private void notifyUserOfPermissionRequestResult() {

    }

    public PermissionState getState() {
        return permissionState;
    }


    private void setState(PermissionState state) {
        permissionState = state;
    }


    public void claimedBy(SystemAdmin admin) {
        if (!getState().equals(PermissionState.REQUESTED) && !getState().equals(PermissionState.UNIX_REQUESTED))
            return;
        willBeHandledBy(admin);
        if (getState().equals(PermissionState.REQUESTED))
            setState(PermissionState.CLAIMED);
        else if (getState().equals(PermissionState.UNIX_REQUESTED))
            setState(PermissionState.UNIX_CLAIMED);
    }

    public void deniedBy(SystemAdmin admin) {
        if (!getState().equals(PermissionState.CLAIMED) && !getState().equals(PermissionState.UNIX_CLAIMED))
            return;
        if (!this.admin.equals(admin))
            return;
        isGranted = false;
        isUnixPermissionGranted = false;
        setState(PermissionState.DENIED);
        notifyUserOfPermissionRequestResult();
    }

    public void grantedBy(SystemAdmin admin) {
        if (!getState().equals(PermissionState.CLAIMED) && !getState().equals(PermissionState.UNIX_CLAIMED))
            return;
        if (!this.admin.equals(admin))
            return;

        if (profile.isUnixPermissionRequired() && getState().equals(PermissionState.UNIX_CLAIMED))
            isUnixPermissionGranted = true;
        else if (profile.isUnixPermissionRequired() &&
                !isUnixPermissionGranted()) {
            setState(PermissionState.UNIX_REQUESTED);
            notifyUnixAdminsOfPermissionRequest();
            return;
        }
        setState(PermissionState.GRANTED);
        isGranted = true;
        notifyUserOfPermissionRequestResult();
    }

    private void notifyUnixAdminsOfPermissionRequest() {

    }

    private boolean isUnixPermissionGranted() {
        return false;
    }
}
