package com.uj.study.replaceStateAlteringConditionalswithState;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午7:46
 * @description：
 * @modified By：
 * @version:
 */
public class PermissionClaimed extends PermissionState {
    public PermissionClaimed(String claimed) {
        super(claimed);
    }

    public void deniedBy(SystemAdmin admin, SystemPermission permission) {
        if (!permission.admin.equals(admin))
            return;
        permission.isGranted = false;
        permission.isUnixPermissionGranted = false;
        permission.setState(PermissionState.DENIED);
        permission.notifyUserOfPermissionRequestResult();
    }

    public void grantedBy(SystemAdmin admin, SystemPermission permission) {
        if (!permission.admin.equals(admin))
            return;

        if (permission.profile.isUnixPermissionRequired() &&
                !permission.isUnixPermissionGranted()) {
            permission.setState(PermissionState.UNIX_REQUESTED);
            permission.notifyUnixAdminsOfPermissionRequest();
            return;
        }
        permission.setState(PermissionState.GRANTED);
        permission.isGranted = true;
        permission.notifyUserOfPermissionRequestResult();
    }
}
