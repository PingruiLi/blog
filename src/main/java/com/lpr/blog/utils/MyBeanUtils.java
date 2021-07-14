package com.lpr.blog.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

public class MyBeanUtils {
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptor = beanWrapper.getPropertyDescriptors();
        List<String> nullAttributes = new ArrayList<>();
        for(PropertyDescriptor pd:propertyDescriptor){
            String name = pd.getName();
            if(beanWrapper.getPropertyValue(name) == null) {
                nullAttributes.add(name);
            }
        }
        return nullAttributes.toArray(new String[nullAttributes.size()]);
    }
}
