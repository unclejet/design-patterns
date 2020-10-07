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
    private boolean granted;

    public SystemPermission() {
        setState(PermissionState.REQUESTED);
        granted = false;
    }


    public void claimed() {
        if (getState().equals(PermissionState.REQUESTED))
            setState(PermissionState.CLAIMED);
    }

    public void denied() {
        if (getState().equals(PermissionState.CLAIMED))
            setState(PermissionState.DENIED);
    }

    public void granted() {
        if (!getState().equals(PermissionState.CLAIMED)) return;
        setState(PermissionState.GRANTED);
        granted = true;
    }

    public boolean isGranted() {
        return granted;
    }

    private void setState(PermissionState permission) {
        this.permission = permission;
    }

    public PermissionState getState() {
        return permission;
    }
}
