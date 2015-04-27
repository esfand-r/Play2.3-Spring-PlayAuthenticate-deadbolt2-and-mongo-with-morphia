package forms.usermanagement;

import play.data.Form;

import static play.data.Form.form;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 */
public class UserManagementForms {
    public static final Form<SignupForm> SIGNUP_FORM = form(SignupForm.class);
    public static final Form<LoginForm> LOGIN_FORM = form(LoginForm.class);
    public static final Form<InvitationForm> INVITE_FORM = form(InvitationForm.class);
    public static final Form<AcceptForm> ACCEPT_FORM = form(AcceptForm.class);
    public static final Form<PasswordChangeForm> PASSWORD_CHANGE_FORM = form(PasswordChangeForm.class);
    public static final Form<IdentityForm> FORGOT_PASSWORD_FORM = form(IdentityForm.class);
    public static final Form<PasswordResetForm> PASSWORD_RESET_FORM = form(PasswordResetForm.class);
}
