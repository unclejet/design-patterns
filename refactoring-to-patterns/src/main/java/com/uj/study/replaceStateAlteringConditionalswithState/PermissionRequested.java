package com.uj.study.replaceStateAlteringConditionalswithState;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午7:45
 * @description：
 * @modified By：
 * @version:
 */
public class PermissionRequested extends PermissionState {
    public PermissionRequested(String requested) {
        super(requested);
    }

    public void claimedBy(SystemAdmin admin, SystemPermission permission) {
        permission.willBeHandledBy(admin);
        permission.setState(CLAIMED);
    }
}
