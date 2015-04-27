package com.mycane.notification.template.generator.content;

import com.mycane.notification.type.NotificationType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by esfandiaramirrahimi on 2015-05-05.
 */
public class VerificationContentGenerator implements ITemplateContentGenerator {
    @Override
    public String renderContent(NotificationType notificationType, Map<String, String> parameters, Class<?> templateClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method;
        String content;

        method = templateClass.getMethod("render", String.class, String.class, String.class, String.class);
        content = method.invoke(null, parameters.get("url"), parameters.get("token"),
                parameters.get("name"), parameters.get("email")).toString();

        return content;
    }
}
