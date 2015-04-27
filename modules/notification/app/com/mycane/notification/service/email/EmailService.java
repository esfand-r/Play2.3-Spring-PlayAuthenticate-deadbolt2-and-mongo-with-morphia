package com.mycane.notification.service.email;

import com.feth.play.module.mail.Mailer;
import com.mycane.notification.service.template.ITemplateGenerator;
import com.mycane.notification.type.NotificationType;
import com.mycane.notification.vo.IMessageContent;
import com.mycane.notification.vo.email.EmailTemplateParametersVO;
import com.mycane.notification.vo.email.InvitationRequestVO;
import com.mycane.notification.vo.email.PasswordResetRequestVO;
import com.mycane.notification.vo.email.VerificationRequestVO;
import com.mycane.security.model.usermanagement.User;
import org.apache.commons.lang3.tuple.Pair;
import play.i18n.Lang;
import play.i18n.Messages;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Named
//todo:clean up next round and abstract duplicate methods
public class EmailService implements IEmailService {
    @Inject
    private ITemplateGenerator emailTemplateGenerator;
    @Inject
    private Mailer mailer;

    @Override
    public void sendInvite(final InvitationRequestVO invitationRequestVO) {
        final Lang lang = Lang.preferred(invitationRequestVO.getAcceptLanguages());

        final String subject = getInviteEmailMailingSubjectAfterSignup(lang);

        final EmailTemplateParametersVO emailTemplateParametersVO = new EmailTemplateParametersVO.Builder().
                withTemplateUrl("invite_email").
                withLangCode(lang.code()).
                withCustomParameter("url", invitationRequestVO.getUrl()).
                withCustomParameter("name", invitationRequestVO.getName())
                .build();

        final IMessageContent iMessageContent = emailTemplateGenerator.getTemplate(emailTemplateParametersVO, NotificationType.VERIFICATION);

        //todo: get list of emails in this method?
        final Mailer.Mail mail = new Mailer.Mail(subject, new Mailer.Mail.Body(iMessageContent.getText(), iMessageContent.getHtml()), new String[]{invitationRequestVO.getEmail()});
        mailer.sendMail(mail);
    }

    @Override
    public void sendVerification(final VerificationRequestVO verificationRequestVO) {
        final String subject = getVerifyEmailMailingSubject(verificationRequestVO.getLang());
        final Pair<String, String> textAndHtml = getVerifyEmailMailingBody(
                verificationRequestVO.getUrl(), verificationRequestVO.getToken(),
                verificationRequestVO.getName(), verificationRequestVO.getEmail(),
                verificationRequestVO.getAcceptLanguages(), verificationRequestVO.isAfterSignup());
        final Mailer.Mail mail = new Mailer.Mail(subject, new Mailer.Mail.Body(textAndHtml.getLeft(), textAndHtml.getRight()),
                new String[]{Mailer.getEmailName(verificationRequestVO.getEmail(), verificationRequestVO.getName())});
        mailer.sendMail(mail);
    }

    @Override
    public void sendPasswordReset(final PasswordResetRequestVO passwordResetRequestVO) {
        final String subject = getPasswordResetMailingSubject(passwordResetRequestVO.getLang());
        final Pair<String, String> textAndHtml = getResetEmailMailingBody(passwordResetRequestVO.getUrl(), passwordResetRequestVO.getToken(),
                passwordResetRequestVO.getUser(), passwordResetRequestVO.getAcceptLanguages());
        final Mailer.Mail mail = new Mailer.Mail(subject, new Mailer.Mail.Body(textAndHtml.getLeft(), textAndHtml.getRight()),
                new String[]{Mailer.getEmailName(passwordResetRequestVO.getUser().getEmail(), passwordResetRequestVO.getUser().getName())});
        mailer.sendMail(mail);
    }

    @Override
    //todo: clean all these crap. Abstract template, take root higher level.
    public Pair<String, String> getVerifyEmailMailingBody(final String url, final String token, final String name, final String email,
                                                          final List<Lang> acceptLanguages, final boolean isAfterSignup) {
        final Lang lang = Lang.preferred(acceptLanguages);

        final String templateUrl = isAfterSignup ? "verify_after_email" : "verify_email";
        final EmailTemplateParametersVO emailTemplateParametersVO = new EmailTemplateParametersVO.Builder().
                withTemplateUrl(templateUrl).
                withLangCode(lang.code()).
                withCustomParameter("url", url).
                withCustomParameter("token", token).
                withCustomParameter("name", name).
                withCustomParameter("email", email).build();

        final IMessageContent messageContent = emailTemplateGenerator.getTemplate(emailTemplateParametersVO, NotificationType.VERIFICATION);
        return Pair.of(messageContent.getText(), messageContent.getHtml());
    }

    private Pair<String, String> getResetEmailMailingBody(final String url, final String token, final User user, final List<Lang> acceptLanguages) {
        final Lang lang = Lang.preferred(acceptLanguages);

        final EmailTemplateParametersVO emailTemplateParametersVO = new EmailTemplateParametersVO.Builder().
                withTemplateUrl("password_reset").
                withLangCode(lang.code()).
                withCustomParameter("url", url).
                withCustomParameter("token", token).
                withCustomParameter("name", user.getName()).
                withCustomParameter("email", user.getEmail()).build();

        final IMessageContent iMessageContent = emailTemplateGenerator.getTemplate(emailTemplateParametersVO, NotificationType.PASSWORD_RESET);
        return Pair.of(iMessageContent.getText(), iMessageContent.getHtml());
    }

    private String getVerifyEmailMailingSubject(final Lang lang) {
        return Messages.get("mycane.password.verify_email.subject", lang);
    }

    private String getPasswordResetMailingSubject(final Lang lang) {
        return Messages.get("mycane.password.reset_email.subject", lang);
    }

    private String getInviteEmailMailingSubjectAfterSignup(Lang lang) {
        return Messages.get(lang, "mycane.invite.subject");
    }
}
