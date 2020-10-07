package com.uj.study.replaceTypeCodewithClass;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午6:37
 * @description：
 * @modified By：
 * @version:
 */
public class PermissionState {
    public final static PermissionState REQUESTED = new PermissionState();

    public final static PermissionState CLAIMED = new PermissionState();

    public final static PermissionState GRANTED = new PermissionState();

    public final static PermissionState DENIED = new PermissionState();
}
