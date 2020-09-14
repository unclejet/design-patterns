package com.uj.study.EncapsulateClasseswithFactory.client;

import com.uj.study.EncapsulateClasseswithFactory.descriptors.AttributeDescriptor;

import java.util.ArrayList;
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
        result.add(AttributeDescriptor.forInteger("remoteId", getClass()));
        result.add(AttributeDescriptor.forDate("createdDate", getClass()));
        result.add(AttributeDescriptor.forDate("lastChangedDate", getClass()));
        result.add(AttributeDescriptor.forInteger("optimisticLockVersion", getClass()));

        result.add(AttributeDescriptor.forUser("createdBy", getClass()));
        result.add(AttributeDescriptor.forUser("lastChangedBy", getClass()));
        return result;
    }
}
