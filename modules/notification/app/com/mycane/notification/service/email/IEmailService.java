package com.mycane.notification.service.email;

import com.mycane.notification.vo.email.InvitationRequestVO;
import com.mycane.notification.vo.email.PasswordResetRequestVO;
import com.mycane.notification.vo.email.VerificationRequestVO;
import org.apache.commons.lang3.tuple.Pair;
import play.i18n.Lang;

import java.util.List;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public interface IEmailService {
    void sendVerification(final VerificationRequestVO verificationRequestVO);

    void sendPasswordReset(final PasswordResetRequestVO passwordResetRequestVO);

    void sendInvite(final InvitationRequestVO invitationRequestVO);

    //todo: Maybe modify in either PlayAuthenticate or its usage so we won't need this method here.
    Pair<String, String> getVerifyEmailMailingBody(final String url, final String token, final String name, final String email,
                                                   List<Lang> acceptLanguages, final boolean isAfterSignup);
}
