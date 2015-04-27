package com.mycane.notification.service.template;

import com.mycane.notification.template.generator.TemplateContentGeneratorFactory;
import com.mycane.notification.type.NotificationType;
import com.mycane.notification.vo.email.EmailContentVO;
import com.mycane.notification.vo.email.EmailTemplateParametersVO;
import play.Logger;

import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Named
public class EmailTemplateGenerator implements ITemplateGenerator {
    //todo: get these from properties?
    private static final String EMAIL_TEMPLATE_FALLBACK_LANGUAGE = "en";
    private static final String htmlTemplatePrefix = "com.mycane.notification.template.signup.html.";
    private static final String txtTemplatePrefix = "com.mycane.notification.template.signup.txt.";

    @Override
    public EmailContentVO getTemplate(final EmailTemplateParametersVO emailTemplateParametersVO, final NotificationType notificationType) {
        String htmlReturn = null;
        String txtReturn = null;

        final Class<?> classForHtml = getTemplateClass(htmlTemplatePrefix + emailTemplateParametersVO.getTemplateUrl(), emailTemplateParametersVO.getLangCode());
        final Class<?> classForTxt = getTemplateClass(txtTemplatePrefix + emailTemplateParametersVO.getTemplateUrl(), emailTemplateParametersVO.getLangCode());

        if (classForHtml != null) {
            htmlReturn = renderContent(notificationType, emailTemplateParametersVO.getCustomParameters(), classForHtml);
        }

        if (classForTxt != null) {
            txtReturn = renderContent(notificationType, emailTemplateParametersVO.getCustomParameters(), classForTxt);
        }

        return new EmailContentVO.Builder().withHtml(htmlReturn).withTxt(txtReturn).build();
    }

    private Class<?> getTemplateClass(final String templateUrl, final String langCode) {
        Class<?> cls = null;

        try {
            cls = Class.forName(templateUrl + "_" + langCode);
        } catch (ClassNotFoundException e) {
            Logger.warn("Template: '" + templateUrl + "_" + langCode
                    + "' was not found! Trying to use English fallback template instead.");
        }
        if (cls == null) {
            try {
                cls = Class.forName(templateUrl + "_" + EMAIL_TEMPLATE_FALLBACK_LANGUAGE);
            } catch (ClassNotFoundException e) {
                Logger.error("Fallback template: '" + langCode + "_" + EMAIL_TEMPLATE_FALLBACK_LANGUAGE
                        + "' was not found either!");
            }
        }
        return cls;
    }

    private String renderContent(final NotificationType notificationType, final Map<String, String> parameters, final Class<?> templateClass) {
        String content = null;
        try {
            content = TemplateContentGeneratorFactory.get(notificationType).renderContent(notificationType, parameters, templateClass);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Logger.error(e.getMessage(), e);
        }
        return content;
    }
}
