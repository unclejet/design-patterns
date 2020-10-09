package com.uj.study.limitInstantiationwithSingleton;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午7:46
 * @description：
 * @modified By：
 * @version:
 */
public class PermissionClaimed extends PermissionState {
    public static final String NAME= "CLAIMED";

    public PermissionClaimed() {
        super(NAME);
    }

    public void deniedBy(SystemAdmin admin, SystemPermission permission) {
        //...
    }

    public void grantedBy(SystemAdmin admin, SystemPermission permission) {
        //...
    }
}
