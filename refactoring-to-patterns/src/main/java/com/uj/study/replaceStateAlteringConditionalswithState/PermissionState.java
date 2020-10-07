package com.uj.study.replaceStateAlteringConditionalswithState;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午7:32
 * @description：
 * @modified By：
 * @version:
 */
public class PermissionState {
    private String name;

    private PermissionState(String name) {
        this.name = name;
    }


    public final static PermissionState REQUESTED = new PermissionState("REQUESTED");

    public final static PermissionState CLAIMED = new PermissionState("CLAIMED");

    public final static PermissionState GRANTED = new PermissionState("GRANTED");

    public final static PermissionState DENIED = new PermissionState("DENIED");

    public final static PermissionState UNIX_REQUESTED = new PermissionState("UNIX_REQUESTED");

    public final static PermissionState UNIX_CLAIMED = new PermissionState("UNIX_CLAIMED");

    public String toString() {
        return name;
    }
}
