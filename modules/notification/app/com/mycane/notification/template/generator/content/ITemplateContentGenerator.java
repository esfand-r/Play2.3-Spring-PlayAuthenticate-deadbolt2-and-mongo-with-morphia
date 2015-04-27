package com.mycane.notification.template.generator.content;

import com.mycane.notification.type.NotificationType;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by esfandiaramirrahimi on 2015-05-05.
 */
public interface ITemplateContentGenerator {
    String renderContent(final NotificationType notificationType, final Map<String, String> parameters, final Class<?> templateClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
