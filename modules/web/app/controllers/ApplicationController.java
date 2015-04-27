package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import com.mycane.security.model.usermanagement.User;
import com.mycane.usermanagement.service.user.IUserService;
import com.mycane.usermanagement.type.RoleType;
import forms.usermanagement.UserManagementForms;
import org.apache.commons.lang3.StringUtils;
import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.restricted;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
//todo: Caching might be good for some of these user objects
@Named
public class ApplicationController extends Controller implements IController {
    @Inject
    private IUserService userService;

    public Result index() {
        String leadId = request().getQueryString("leadId");
        if (StringUtils.isNotBlank(leadId)) {
            response().setCookie("leadId", leadId);
        }
        return ok(index.render(UserManagementForms.SIGNUP_FORM, leadId));
    }

    @Restrict(@Group(RoleType.USER))
    public Result restricted() {
        final AuthUser currentAuthUser = PlayAuthenticate.getUser(session());
        final User localUser = userService.getLocalUser(currentAuthUser);
        return ok(restricted.render(localUser));
    }

    public static Result jsRoutes() {
        return ok(Routes.javascriptRouter("jsRoutes", routes.javascript.UserAccountController.forgotPassword())).
                as("text/javascript");
    }

    public static String formatTimestamp(final long time) {
        return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date(time));
    }
}
