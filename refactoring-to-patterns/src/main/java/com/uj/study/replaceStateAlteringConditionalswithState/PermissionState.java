package com.uj.study.replaceStateAlteringConditionalswithState;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午7:32
 * @description：
 * @modified By：
 * @version:
 */
public abstract class PermissionState {
    private String name;

    protected PermissionState(String name) {
        this.name = name;
    }

    public void claimedBy(SystemAdmin admin, SystemPermission permission) {
        if (!permission.getState().equals(REQUESTED) &&
                !permission.getState().equals(UNIX_REQUESTED))
            return;
        permission.willBeHandledBy(admin);
        if (permission.getState().equals(REQUESTED))
            permission.setState(CLAIMED);
        else if (permission.getState().equals(UNIX_REQUESTED)) {
            permission.setState(UNIX_CLAIMED);
        }
    }

    public void deniedBy(SystemAdmin admin, SystemPermission permission) {
        if (!permission.getState().equals(PermissionState.CLAIMED) && !permission.getState().equals(PermissionState.UNIX_CLAIMED))
            return;
        if (!admin.equals(admin))
            return;
        permission.isGranted = false;
        permission.isUnixPermissionGranted = false;
        permission.setState(PermissionState.DENIED);
        permission.notifyUserOfPermissionRequestResult();
    }

    public void grantedBy(SystemAdmin admin, SystemPermission permission) {
        if (!permission.getState().equals(PermissionState.CLAIMED) && !permission.getState().equals(PermissionState.UNIX_CLAIMED))
            return;
        if (!admin.equals(admin))
            return;

        if (permission.profile.isUnixPermissionRequired() && permission.getState().equals(PermissionState.UNIX_CLAIMED))
            permission.isUnixPermissionGranted = true;
        else if (permission.profile.isUnixPermissionRequired() &&
                !permission.isUnixPermissionGranted()) {
            permission.setState(PermissionState.UNIX_REQUESTED);
            permission.notifyUnixAdminsOfPermissionRequest();
            return;
        }
        permission.setState(PermissionState.GRANTED);
        permission.isGranted = true;
        permission.notifyUserOfPermissionRequestResult();
    }


    public final static PermissionState REQUESTED = new PermissionRequested("REQUESTED");

    public final static PermissionState CLAIMED = new PermissionClaimed("CLAIMED");

    public final static PermissionState GRANTED = new PermissionGranted("GRANTED");

    public final static PermissionState DENIED = new PermissionDenied("DENIED");

    public final static PermissionState UNIX_REQUESTED = new UnixPermissionRequested("UNIX_REQUESTED");

    public final static PermissionState UNIX_CLAIMED = new UnixPermissionClaimed("UNIX_CLAIMED");

    public String toString() {
        return name;
    }
}
