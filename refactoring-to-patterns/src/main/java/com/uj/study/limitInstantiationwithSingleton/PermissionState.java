package com.uj.study.limitInstantiationwithSingleton;

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
    public String toString() {
        return name;
    }
}
