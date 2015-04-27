package providers;

import com.feth.play.module.mail.Mailer;
import com.feth.play.module.mail.Mailer.Mail.Body;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.mycane.notification.service.email.IEmailService;
import com.mycane.security.model.usermanagement.*;
import com.mycane.security.model.usermanagement.TokenAction.Type;
import com.mycane.usermanagement.service.account.ITokenService;
import com.mycane.usermanagement.service.user.IUserService;
import controllers.routes;
import forms.usermanagement.LoginForm;
import forms.usermanagement.SignupForm;
import forms.usermanagement.UserManagementForms;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import play.Application;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Call;
import play.mvc.Http.Context;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Http.Context.Implicit.request;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 * <p>
 * Based on and brought partly untouched from <a href="https://github.com/joscha/play-authenticate">Joscha Feth's PlayAuthenticate</a>
 */
public class MyCaneUsernamePasswordAuthProvider extends UsernamePasswordAuthProvider<String, MyCaneLoginUsernamePasswordAuthUser, MyCaneUsernamePasswordAuthUser, LoginForm, SignupForm> {
    private static final String SETTING_KEY_VERIFICATION_LINK_SECURE = SETTING_KEY_MAIL + "." + "verificationLink.secure";
    private static final String SETTING_KEY_PASSWORD_RESET_LINK_SECURE = SETTING_KEY_MAIL + "." + "passwordResetLink.secure";
    private static final String SETTING_KEY_LINK_LOGIN_AFTER_PASSWORD_RESET = "loginAfterPasswordReset";

    private IUserService userService;
    private ITokenService tokenService;
    private IEmailService emailService;

    public MyCaneUsernamePasswordAuthProvider(Application app) {
        super(app);
    }

    public static MyCaneUsernamePasswordAuthProvider getProvider() {
        return (MyCaneUsernamePasswordAuthProvider) PlayAuthenticate.getProvider(UsernamePasswordAuthProvider.PROVIDER_KEY);
    }

    @Override
    protected List<String> neededSettingKeys() {
        final List<String> needed = new ArrayList<>(super.neededSettingKeys());
        needed.add(SETTING_KEY_VERIFICATION_LINK_SECURE);
        needed.add(SETTING_KEY_PASSWORD_RESET_LINK_SECURE);
        needed.add(SETTING_KEY_LINK_LOGIN_AFTER_PASSWORD_RESET);
        return needed;
    }

    @Override
    protected Form<SignupForm> getSignupForm() {
        return UserManagementForms.SIGNUP_FORM;
    }

    @Override
    protected Form<LoginForm> getLoginForm() {
        return UserManagementForms.LOGIN_FORM;
    }

    @Override
    protected com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.SignupResult signupUser(final MyCaneUsernamePasswordAuthUser user) {
        final User localUser = userService.findByUsernamePasswordIdentity(user);
        if (localUser != null) {
            if (localUser.isEmailValidated()) {
                return SignupResult.USER_EXISTS;
            } else {
                return SignupResult.USER_EXISTS_UNVERIFIED;
            }
        }
        final String leadId = request().cookie("leadId") != null ? request().cookie("leadId").value() : StringUtils.EMPTY;

        //todo: tighter exception mechanism at services?
        userService.create(user);
        return SignupResult.USER_CREATED_UNVERIFIED;
    }

    @Override
    protected com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.LoginResult loginUser(final MyCaneLoginUsernamePasswordAuthUser authUser) {
        final User localUser = userService.findByUsernamePasswordIdentity(authUser);
        if (localUser == null) {
            return LoginResult.NOT_FOUND;
        } else {
            if (!localUser.isEmailValidated()) {
                return LoginResult.USER_UNVERIFIED;
            } else {
                for (final LinkedAccount linkedAccount : localUser.getLinkedAccounts()) {
                    if (getKey().equals(linkedAccount.getProviderKey())) {
                        if (authUser.checkPassword(linkedAccount.getProviderUserId(), authUser.getPassword())) {
                            return LoginResult.USER_LOGGED_IN;
                        } else {
                            return LoginResult.WRONG_PASSWORD;
                        }
                    }
                }
                return LoginResult.WRONG_PASSWORD;
            }
        }
    }

    @Override
    protected Call userExists(final UsernamePasswordAuthUser authUser) {
        return routes.UserAccountController.exists();
    }

    @Override
    protected Call userUnverified(final UsernamePasswordAuthUser authUser) {
        return routes.UserAccountController.unverified();
    }

    @Override
    protected MyCaneUsernamePasswordAuthUser buildSignupAuthUser(final SignupForm signup, final Context ctx) {
        return new MyCaneUsernamePasswordAuthUser(signup.getName(), signup.getPassword(), signup.getEmail());
    }

    @Override
    protected MyCaneLoginUsernamePasswordAuthUser buildLoginAuthUser(final LoginForm login, final Context ctx) {
        return new MyCaneLoginUsernamePasswordAuthUser(login.getPassword(), login.getEmail());
    }

    @Override
    protected MyCaneLoginUsernamePasswordAuthUser transformAuthUser(final MyCaneUsernamePasswordAuthUser authUser, final Context context) {
        return new MyCaneLoginUsernamePasswordAuthUser(authUser.getEmail());
    }

    @Override
    protected String getVerifyEmailMailingSubject(final MyCaneUsernamePasswordAuthUser user, final Context ctx) {
        return Messages.get("mycane.password.verify_signup.subject");
    }

    @Override
    protected String onLoginUserNotFound(final Context context) {
        context.flash().put(controllers.ApplicationController.FLASH_ERROR_KEY, Messages.get("mycane.password.login.unknown_user_or_pw"));
        return super.onLoginUserNotFound(context);
    }

    @Override
    protected Body getVerifyEmailMailingBody(final String token, final MyCaneUsernamePasswordAuthUser user, final Context ctx) {
        final boolean isSecure = getConfiguration().getBoolean(SETTING_KEY_VERIFICATION_LINK_SECURE);
        final String url = routes.UserAccountController.verify(token).absoluteURL(ctx.request(), isSecure);

        final User localUser = userService.getLocalUser(user);
        final Pair<String, String> textAndHtml = emailService.getVerifyEmailMailingBody(url, token, localUser.getName(), localUser.getEmail(), ctx.request().acceptLanguages(), false);

        return new Body(textAndHtml.getLeft(), textAndHtml.getRight());
    }

    @Override
    protected String generateVerificationRecord(final MyCaneUsernamePasswordAuthUser user) {
        final User localUser = userService.findByAuthUserIdentity(user);
        final TokenAction tokenAction = tokenService.create(Type.EMAIL_VERIFICATION, localUser);
        return tokenAction.getToken();
    }

    public boolean isLoginAfterPasswordReset() {
        return getConfiguration().getBoolean(SETTING_KEY_LINK_LOGIN_AFTER_PASSWORD_RESET);
    }

    public void setEmailService(final IEmailService emailService) {
        this.emailService = emailService;
    }

    public void setTokenService(final ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void setUserService(final IUserService userService) {
        this.userService = userService;
    }

    public void setMailer(final Mailer mailer) {
        this.mailer = mailer;
    }
}
