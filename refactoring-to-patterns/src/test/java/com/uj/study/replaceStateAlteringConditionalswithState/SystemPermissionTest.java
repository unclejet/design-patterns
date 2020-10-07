package com.uj.study.replaceStateAlteringConditionalswithState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/8 上午7:10
 * @description：
 * @modified By：
 * @version:
 */
class SystemPermissionTest {
    private SystemPermission permission;
    private SystemUser user = new SystemUser();
    private SystemProfile profile = new SystemProfile();
    private SystemAdmin admin = new SystemAdmin();

    @BeforeEach
    void setUp() {
        permission = new SystemPermission(user, profile);
    }

    @Test
    public void testGrantedBy() {
        permission.grantedBy(admin);
        assertEquals(PermissionState.REQUESTED, permission.getState());
        assertEquals( false, permission.isGranted);
        permission.claimedBy(admin);
        permission.grantedBy(admin);
        assertEquals( PermissionState.GRANTED, permission.getState());
        assertEquals(true, permission.isGranted);
    }
}