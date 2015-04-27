package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import com.mycane.notification.service.email.IEmailService;
import com.mycane.notification.vo.email.InvitationRequestVO;
import com.mycane.notification.vo.email.PasswordResetRequestVO;
import com.mycane.notification.vo.email.VerificationRequestVO;
import com.mycane.security.model.usermanagement.MyCaneLoginUsernamePasswordAuthUser;
import com.mycane.security.model.usermanagement.MyCaneUsernamePasswordAuthUser;
import com.mycane.security.model.usermanagement.TokenAction;
import com.mycane.security.model.usermanagement.User;
import com.mycane.usermanagement.service.account.IAccountService;
import com.mycane.usermanagement.service.account.ITokenService;
import com.mycane.usermanagement.service.user.IUserService;
import com.mycane.usermanagement.type.RoleType;
import forms.usermanagement.*;
import org.apache.commons.lang3.StringUtils;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyCaneUsernamePasswordAuthProvider;
import views.html.account.*;
import views.html.account.signup.exists;
import views.html.account.signup.no_token_or_invalid;
import views.html.account.signup.password_forgot;
import views.html.account.signup.password_reset;
import views.html.invite;
import views.html.login;
import views.html.profile;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 * <p>
 * Based on and brought partly untouched from <a href="https://github.com/joscha/play-authenticate">Joscha Feth's PlayAuthenticate</a>
 */
@Named
public class UserAccountController extends Controller implements IController {
    @Inject
    private IUserService userService;
    @Inject
    private IAccountService accountService;
    @Inject
    private ITokenService tokenService;
    @Inject
    private IEmailService iEmailService;

    @SubjectPresent
    public Result link() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        return ok(link.render());
    }

    @Restrict(@Group(RoleType.USER))
    public Result verifyEmail() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
        final User localUser = userService.getLocalUser(currentAuthUser);

        if (localUser.isEmailValidated()) {
            flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.verify_email.error.already_validated"));
        } else if (localUser.getEmail() != null && !localUser.getEmail().trim().isEmpty()) {
            flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.verify_email.message.instructions_sent", localUser.getEmail()));

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
        } else {
            flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.verify_email.error.set_email_first", localUser.getEmail()));
        }
        return redirect(routes.UserAccountController.profile());
    }

    @Restrict(@Group(RoleType.USER))
    public Result changePassword() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
        final User localUser = userService.getLocalUser(currentAuthUser);

        if (!localUser.isEmailValidated()) {
            return ok(unverified.render());
        } else {
            return ok(password_change.render(UserManagementForms.PASSWORD_CHANGE_FORM));
        }
    }

    @Restrict(@Group(RoleType.USER))
    public Result doChangePassword() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final Form<PasswordChangeForm> filledForm = UserManagementForms.PASSWORD_CHANGE_FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(password_change.render(filledForm));
        } else {
            final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
            final User localUser = userService.getLocalUser(currentAuthUser);
            final String newPassword = filledForm.get().getPassword();

            accountService.changePassword(localUser, new MyCaneUsernamePasswordAuthUser(newPassword), true);

            flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.change_password.success"));
            return redirect(routes.UserAccountController.profile());
        }
    }

    @SubjectPresent
    public Result askLink() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final AuthUser authUser = PlayAuthenticate.getLinkUser(session());
        if (authUser == null) {
            return redirect(routes.ApplicationController.index());
        }
        return ok(ask_link.render(UserManagementForms.ACCEPT_FORM, authUser));
    }

    @SubjectPresent
    public Result doLink() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final AuthUser authUser = PlayAuthenticate.getLinkUser(session());
        if (authUser == null) {
            return redirect(routes.ApplicationController.index());
        }

        final Form<AcceptForm> filledForm = UserManagementForms.ACCEPT_FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(ask_link.render(filledForm, authUser));
        } else {
            final boolean link = filledForm.get().getAccept();
            if (link) {
                flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.accounts.link.success"));
            }
            return PlayAuthenticate.link(ctx(), link);
        }
    }

    @SubjectPresent
    public Result askMerge() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final AuthUser aUser = PlayAuthenticate.getUser(session());

        final AuthUser bUser = PlayAuthenticate.getMergeUser(session());
        if (bUser == null) {
            return redirect(routes.ApplicationController.index());
        }
        return ok(ask_merge.render(UserManagementForms.ACCEPT_FORM, aUser, bUser));
    }

    @SubjectPresent
    public Result doMerge() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final AuthUser aUser = PlayAuthenticate.getUser(session());

        final AuthUser bUser = PlayAuthenticate.getMergeUser(session());
        if (bUser == null) {
            return redirect(routes.ApplicationController.index());
        }

        final Form<AcceptForm> filledForm = UserManagementForms.ACCEPT_FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(ask_merge.render(filledForm, aUser, bUser));
        } else {
            final boolean merge = filledForm.get().getAccept();
            if (merge) {
                flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.accounts.merge.success"));
            }
            return PlayAuthenticate.merge(ctx(), merge);
        }
    }

    public Result unverified() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        return ok(views.html.account.signup.unverified.render());
    }

    public Result forgotPassword(final String email) {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        Form<IdentityForm> form = UserManagementForms.FORGOT_PASSWORD_FORM;
        if (email != null && !email.trim().isEmpty()) {
            form = UserManagementForms.FORGOT_PASSWORD_FORM.fill(new IdentityForm(email));
        }
        return ok(password_forgot.render(form));
    }

    public Result doForgotPassword() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final Form<IdentityForm> filledForm = UserManagementForms.FORGOT_PASSWORD_FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(password_forgot.render(filledForm));
        } else {
            final String email = filledForm.get().getEmail();

            flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.reset_password.message.instructions_sent", email));

            final User localUser = userService.findByEmail(email);
            //Check if user already exists.
            if (localUser != null) {
                if (localUser.isEmailValidated()) {
                    final TokenAction tokenAction = tokenService.create(TokenAction.Type.PASSWORD_RESET, localUser);

                    PasswordResetRequestVO passwordResetRequestVO = new PasswordResetRequestVO.Builder().
                            withUrl(ctx().request().host()).
                            withAcceptLanguages(ctx().request().acceptLanguages()).
                            withToken(tokenAction.getToken()).
                            withUser(localUser).
                            withLang(ctx().lang()).
                            build();

                    iEmailService.sendPasswordReset(passwordResetRequestVO);
                } else {
                    flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.reset_password.message.email_not_verified"));

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
                }
            }

            return redirect(routes.ApplicationController.index());
        }
    }

    public Result resetPassword(final String token) {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final TokenAction tokenAction = tokenIsValid(token, TokenAction.Type.PASSWORD_RESET);
        if (tokenAction == null) {
            return badRequest(no_token_or_invalid.render());
        }

        return ok(password_reset.render(UserManagementForms.PASSWORD_RESET_FORM.fill(new PasswordResetForm(token))));
    }

    public Result doResetPassword() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final Form<PasswordResetForm> filledForm = UserManagementForms.PASSWORD_RESET_FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(password_reset.render(filledForm));
        } else {
            final String token = filledForm.get().getToken();
            final String newPassword = filledForm.get().getPassword();

            final TokenAction tokenAction = tokenIsValid(token, TokenAction.Type.PASSWORD_RESET);
            if (tokenAction == null) {
                return badRequest(no_token_or_invalid.render());
            }

            final User user = tokenAction.getTargetUser();

            try {
                // We can pass true for the second parameter if you want to automatically create a password.
                accountService.resetPassword(user, new MyCaneUsernamePasswordAuthUser(newPassword), false);
            } catch (final RuntimeException re) {
                flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.reset_password.message.no_password_account"));
            }

            final boolean login = MyCaneUsernamePasswordAuthProvider.getProvider().isLoginAfterPasswordReset();
            if (login) {
                flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.reset_password.message.success.auto_login"));
                return PlayAuthenticate.loginAndRedirect(ctx(), new MyCaneLoginUsernamePasswordAuthUser(user.getEmail()));
            } else {
                flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.reset_password.message.success.manual_login"));
            }

            return redirect(routes.AuthenticationController.login());
        }
    }

    public Result verify(final String token) {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final TokenAction tokenAction = tokenIsValid(token, TokenAction.Type.EMAIL_VERIFICATION);
        if (tokenAction == null) {
            return badRequest(no_token_or_invalid.render());
        }
        final String email = tokenAction.getTargetUser().getEmail();
        accountService.verify(tokenAction.getTargetUser());
        flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get("mycane.verify_email.success", email));
        final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
        final User localUser = userService.getLocalUser(currentAuthUser);
        if (localUser != null) {
            return redirect(routes.ApplicationController.index());
        } else {
            return redirect(routes.AuthenticationController.login());
        }
    }

    private TokenAction tokenIsValid(final String token, final TokenAction.Type type) {
        TokenAction returnTokenAction = null;
        if (token != null && !token.trim().isEmpty()) {
            final TokenAction tokenAction = tokenService.findByToken(token, type);
            if (tokenAction != null && tokenAction.isValid()) {
                returnTokenAction = tokenAction;
            }
        }
        return returnTokenAction;
    }


    public Result exists() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        return ok(exists.render());
    }

    @Restrict(@Group(RoleType.USER))
    public Result profile() {
        final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
        final User localUser = userService.getLocalUser(currentAuthUser);
        return ok(profile.render(localUser));
    }

    @Restrict(@Group(RoleType.USER))
    public Result invite() {
        final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
        final User localUser = userService.getLocalUser(currentAuthUser);
        return ok(invite.render(localUser, UserManagementForms.INVITE_FORM));
    }

    public Result doInvite() {
        final Form<InvitationForm> filledForm = UserManagementForms.INVITE_FORM.bindFromRequest();

        InvitationForm invite = filledForm.get();
        if (filledForm.hasErrors()) {
            return badRequest(login.render(filledForm));
        } else {
            final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
            final User localUser = userService.getLocalUser(currentAuthUser);
            invite.getInvites().stream().filter(inviteDetail -> StringUtils.isNotBlank(inviteDetail.getName()) && StringUtils.isNotBlank(inviteDetail.getEmail())).forEach(inviteDetail -> {

                String url = routes.UserController.signup().absoluteURL(isSecure, ctx().request().host()) + "?leadId=" + localUser.getIdentifier();

                InvitationRequestVO invitationRequestVO = new InvitationRequestVO.Builder().
                        withUrl(url).
                        withAcceptLanguages(ctx().request().acceptLanguages()).
                        withName(localUser.getName()).
                        withEmail(localUser.getEmail()).
                        withLang(ctx().lang()).
                        build();

                iEmailService.sendInvite(invitationRequestVO);
            });
            return ok();
        }
    }
}
