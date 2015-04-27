package com.mycane.notification.template.generator;

import com.mycane.notification.template.generator.content.ITemplateContentGenerator;
import com.mycane.notification.template.generator.content.InvitationContentGenerator;
import com.mycane.notification.template.generator.content.PasswordResetContentGenerator;
import com.mycane.notification.template.generator.content.VerificationContentGenerator;
import com.mycane.notification.type.NotificationType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by esfandiaramirrahimi on 2015-05-05.
 */
public class TemplateContentGeneratorFactory {
    //Configure Template Generator based on the notification type.
    private static final Map<NotificationType, Class<? extends ITemplateContentGenerator>> IMPLEMENTATIONS =
            new HashMap<NotificationType, Class<? extends ITemplateContentGenerator>>() {{
                put(NotificationType.INVITATION, InvitationContentGenerator.class);
                put(NotificationType.PASSWORD_RESET, PasswordResetContentGenerator.class);
                put(NotificationType.VERIFICATION, VerificationContentGenerator.class);
            }};

    public static <T extends ITemplateContentGenerator> T get(final NotificationType notificationType) throws InstantiationException, IllegalAccessException {
        return (T) IMPLEMENTATIONS.get(notificationType).newInstance();
    }
}

