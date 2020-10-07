package com.uj.study.replaceTypeCodewithClass;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午6:34
 * @description：
 * @modified By：
 * @version:
 */
public class SystemPermission {
    private String state;
    private boolean granted;

    public final static String REQUESTED = "REQUESTED";
    public final static String CLAIMED = "CLAIMED";
    public final static String DENIED = "DENIED";
    public final static String GRANTED = "GRANTED";

    public SystemPermission() {
        state = REQUESTED;
        granted = false;
    }

    public void claimed() {
        if (state.equals(REQUESTED))
            state = CLAIMED;
    }

    public void denied() {
        if (state.equals(CLAIMED))
            state = DENIED;
    }

    public void granted() {
        if (!state.equals(CLAIMED)) return;
        state = GRANTED;
        granted = true;
    }

    public boolean isGranted() {
        return granted;
    }

    public String getState() {
        return state;
    }
}
