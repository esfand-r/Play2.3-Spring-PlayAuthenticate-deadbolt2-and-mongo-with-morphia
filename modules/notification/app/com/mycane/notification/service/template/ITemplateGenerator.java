package com.mycane.notification.service.template;

import com.mycane.notification.type.NotificationType;
import com.mycane.notification.vo.IMessageContent;
import com.mycane.notification.vo.email.EmailTemplateParametersVO;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public interface ITemplateGenerator {
    IMessageContent getTemplate(final EmailTemplateParametersVO emailTemplateParametersVO, final NotificationType contentType);
}
