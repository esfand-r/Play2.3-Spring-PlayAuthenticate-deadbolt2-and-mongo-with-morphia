package com.mycane.notification.template.generator.content;

import com.mycane.notification.type.NotificationType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by esfandiaramirrahimi on 2015-05-05.
 */
public class PasswordResetContentGenerator implements ITemplateContentGenerator {
    @Override
    public String renderContent(final NotificationType notificationType, final Map<String, String> parameters, final Class<?> templateClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method method;
        final String content;

        method = templateClass.getMethod("render", String.class, String.class, String.class, String.class);
        content = method.invoke(null, parameters.get("url"), parameters.get("token"),
                parameters.get("name"), parameters.get("email")).toString();

        return content;
    }
}
