package com.uj.study.replaceTypeCodewithClass;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午6:34
 * @description：
 * @modified By：
 * @version:
 */
public class SystemPermission {
    private PermissionState permission;
    private String state;
    private boolean granted;

    public final static String REQUESTED = "REQUESTED";
    public final static String CLAIMED = "CLAIMED";
    public final static String DENIED = "DENIED";
    public final static String GRANTED = "GRANTED";


    public SystemPermission() {
        setState(REQUESTED);
        setState(PermissionState.REQUESTED);
        granted = false;
    }


    public void claimed() {
        if (state.equals(REQUESTED))
            setState(CLAIMED);
            setState(PermissionState.CLAIMED);
    }

    public void denied() {
        if (state.equals(CLAIMED))
            setState(DENIED);
            setState(PermissionState.DENIED);
    }

    public void granted() {
        if (!state.equals(CLAIMED)) return;
        setState(GRANTED);
        setState(PermissionState.GRANTED);
        granted = true;
    }

    public boolean isGranted() {
        return granted;
    }

    private void setState(String state) {
        this.state = state;
    }

    private void setState(PermissionState permission) {
        this.permission = permission;
    }

    public String getState() {
        return permission.toString();
    }
}
