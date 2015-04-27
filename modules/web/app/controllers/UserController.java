package controllers;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import com.mycane.notification.service.email.IEmailService;
import com.mycane.usermanagement.service.user.IUserService;
import forms.usermanagement.SignupForm;
import forms.usermanagement.UserManagementForms;
import org.apache.commons.lang3.StringUtils;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.signup;

import javax.inject.Inject;
import javax.inject.Named;

import static com.feth.play.module.pa.controllers.Authenticate.noCache;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 * <p>
 * Based on and brought partly untouched from <a href="https://github.com/joscha/play-authenticate">Joscha Feth's PlayAuthenticate</a>
 */
@Named
public class UserController extends Controller implements IController {
    @Inject
    private IUserService userService;
    @Inject
    private IEmailService iEmailService;

    public Result signup() {
        String leadId = request().getQueryString("leadId");
        if (StringUtils.isNotBlank(leadId)) {
            response().setCookie("leadId", leadId);
        }
        return ok(signup.render(UserManagementForms.SIGNUP_FORM, leadId));
    }

    public Result doSignup(String leadId) {
        String lead = request().cookie("leadId") != null ? request().cookie("leadId").value() : StringUtils.EMPTY;
        noCache(response());
        final Form<SignupForm> filledForm = UserManagementForms.SIGNUP_FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(signup.render(filledForm, lead));
        } else {
            return UsernamePasswordAuthProvider.handleSignup(ctx());
        }
    }
}
