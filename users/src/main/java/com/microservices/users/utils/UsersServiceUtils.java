package com.microservices.users.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import io.sentry.Sentry;
import io.sentry.SentryLevel;

public class UsersServiceUtils {
    private static final Logger logger = Logger.getLogger(UsersServiceUtils.class.getName());

    // Get names of properties which are null in the updatedUser
    public static String[] getNullPropertyNames(Object source) {
        try {
            final BeanWrapper src = new BeanWrapperImpl(source);
            java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

            Set<String> emptyNames = new HashSet<>();
            for (java.beans.PropertyDescriptor pd : pds) {
                Object srcValue = src.getPropertyValue(pd.getName());
                if (srcValue == null)
                    emptyNames.add(pd.getName());
            }

            String[] result = new String[emptyNames.size()];
            return emptyNames.toArray(result);
        } catch (Exception e) {
            Sentry.captureMessage("Error occurred while getting null property names: " + e.getMessage(),
                    SentryLevel.ERROR);
            logger.severe("Error occurred while getting null property names: " + e.getMessage());
            return null;
        }
    }
}
