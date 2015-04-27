package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import com.feth.play.module.pa.user.AuthUser;
import com.mycane.notification.service.email.IEmailService;
import com.mycane.notification.vo.email.VerificationRequestVO;
import com.mycane.security.model.usermanagement.User;
import com.mycane.usermanagement.service.account.IAccountService;
import com.mycane.usermanagement.service.user.IUserService;
import forms.usermanagement.LoginForm;
import forms.usermanagement.UserManagementForms;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.signup.oAuthDenied;
import views.html.login;

import javax.inject.Inject;
import javax.inject.Named;

import static com.feth.play.module.pa.controllers.Authenticate.noCache;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 * <p>
 * Based on and brought partly untouched from <a href="https://github.com/joscha/play-authenticate">Joscha Feth's PlayAuthenticate</a>
 */
@Named
public class AuthenticationController extends Controller implements IController {
    @Inject
    private IEmailService iEmailService;
    @Inject
    private IUserService userService;
    @Inject
    private IAccountService accountService;

    public Result login() {
        return ok(login.render(UserManagementForms.LOGIN_FORM));
    }

    public Result authenticate(final String provider) {
        noCache(response());
        final String payload = request().getQueryString("p");
        Result result = PlayAuthenticate.handleAuthentication(provider, ctx(), payload);
        final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
        final User localUser = userService.getLocalUser(currentAuthUser);
        if (localUser != null && !localUser.isEmailValidated()) {
            final String token = accountService.generateVerificationRecord(localUser);
            final String url = routes.UserAccountController.verify(token).absoluteURL(isSecure, ctx().request().host());

            VerificationRequestVO verificationRequestVO = new VerificationRequestVO.Builder().
                    withUrl(url).
                    withEmail(localUser.getEmail()).
                    withName(localUser.getName()).
                    withAcceptLanguages(ctx().request().acceptLanguages()).
                    withToken(token).
                    withIsAfterSignup(false).
                    withLang(ctx().lang()).
                    build();

            iEmailService.sendVerification(verificationRequestVO);
            return ok(views.html.profile.render(localUser));
        }
        return result;
    }

    public Result doLogin() {
        noCache(response());
        final Form<LoginForm> filledForm = UserManagementForms.LOGIN_FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(login.render(filledForm));
        } else {
            return UsernamePasswordAuthProvider.handleLogin(ctx());
        }
    }

    public Result oAuthDenied(final String getProviderKey) {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        return ok(oAuthDenied.render(getProviderKey));
    }
}
