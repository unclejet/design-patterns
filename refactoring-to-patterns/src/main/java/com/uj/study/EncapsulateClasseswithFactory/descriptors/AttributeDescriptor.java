package com.uj.study.EncapsulateClasseswithFactory.descriptors;

import com.uj.study.EncapsulateClasseswithFactory.user.RemoteUser;
import com.uj.study.EncapsulateClasseswithFactory.user.User;

import java.util.Date;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/8 上午6:49
 * @description：
 * @modified By：
 * @version:
 */
public abstract class AttributeDescriptor {
    protected AttributeDescriptor(String description, Object aClass, Object type) {
        //...
    }

    protected AttributeDescriptor(String description, Object aClass, Object type, Object subType) {
        //...
    }

    public static DefaultDescriptor forInteger(String description, Object aClass) {
        return new DefaultDescriptor(description, aClass, Integer.TYPE);
    }

    public static DefaultDescriptor forDate(String description, Object aClass) {
        return new DefaultDescriptor(description, aClass, Date.class);
    }

    public static ReferenceDescriptor forUser(String description, Object aClass) {
        return new ReferenceDescriptor(description, aClass, User.class, RemoteUser.class);
    }
}
