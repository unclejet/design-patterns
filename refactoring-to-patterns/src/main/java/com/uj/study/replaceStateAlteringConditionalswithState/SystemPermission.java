package com.uj.study.replaceStateAlteringConditionalswithState;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午7:10
 * @description：
 * @modified By：
 * @version:
 */
public class SystemPermission {
    SystemProfile profile;
    SystemUser requestor;
    SystemAdmin admin;
    boolean isGranted;
    boolean isUnixPermissionGranted;
    private PermissionState permission;

    public SystemPermission(SystemUser requestor, SystemProfile profile) {
        this.requestor = requestor;
        this.profile = profile;
        setState(PermissionState.REQUESTED);
        isGranted = false;
        notifyAdminOfPermissionRequest();
    }

    private void notifyAdminOfPermissionRequest() {

    }

    void willBeHandledBy(SystemAdmin admin) {
        this.admin = admin;
    }

    void notifyUserOfPermissionRequestResult() {

    }

    public PermissionState getState() {
        return permission;
    }


    void setState(PermissionState state) {
        permission = state;
    }


    public void claimedBy(SystemAdmin admin) {
        permission.claimedBy(admin, this);
    }

    public void deniedBy(SystemAdmin admin) {
        permission.deniedBy(admin, this);
    }

    public void grantedBy(SystemAdmin admin) {
        permission.grantedBy(admin, this);
    }

    void notifyUnixAdminsOfPermissionRequest() {

    }

    boolean isUnixPermissionGranted() {
        return false;
    }
}
