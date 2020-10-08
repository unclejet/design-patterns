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
    }

    public void deniedBy(SystemAdmin admin, SystemPermission permission) {
    }

    public void grantedBy(SystemAdmin admin, SystemPermission permission) {
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
