package com.uj.study.limitInstantiationwithSingleton;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午7:45
 * @description：
 * @modified By：
 * @version:
 */
public class PermissionRequested extends PermissionState {
    public static final String NAME= "REQUESTED";

    private static PermissionState state = new PermissionRequested();

    public String name() {
        return NAME;
    }

    public static PermissionState state() {
        return state;
    }

    public PermissionRequested() {
        super(NAME);
    }

    public void claimedBy(SystemAdmin admin, SystemPermission permission) {
        permission.willBeHandledBy(admin);
        permission.setState(new PermissionClaimed());
    }
}
