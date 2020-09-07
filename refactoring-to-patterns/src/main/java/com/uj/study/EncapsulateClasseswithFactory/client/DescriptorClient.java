package com.uj.study.EncapsulateClasseswithFactory.client;

import com.uj.study.EncapsulateClasseswithFactory.descriptors.DefaultDescriptor;
import com.uj.study.EncapsulateClasseswithFactory.descriptors.ReferenceDescriptor;
import com.uj.study.EncapsulateClasseswithFactory.user.RemoteUser;
import com.uj.study.EncapsulateClasseswithFactory.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：unclejet
 * @date ：Created in 2020/9/8 上午6:52
 * @description：
 * @modified By：
 * @version:
 */
public class DescriptorClient {
    protected List createAttributeDescriptors() {
        List result = new ArrayList();
        result.add(new DefaultDescriptor("remoteId", getClass(), Integer.TYPE));
        result.add(new DefaultDescriptor("createdDate", getClass(), Date.class));
        result.add(new DefaultDescriptor("lastChangedDate", getClass(), Date.class));
        result.add(new DefaultDescriptor("optimisticLockVersion", getClass(), Integer.TYPE));

        result.add(new ReferenceDescriptor("createdBy", getClass(), User.class, RemoteUser.class));
        result.add(new ReferenceDescriptor("lastChangedBy", getClass(), User.class, RemoteUser.class));
        return result;
    }
}
