package com.taskengine.taskengine_task_service.utils;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.Objects;

public class NullCheckUtil {

    public static String[] getNullPropertyNames(Object source){
        final BeanWrapper src= new BeanWrapperImpl(source);

        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name-> Objects.isNull(src.getPropertyValue(name)))
                .toArray(String[]::new);
    }
}
