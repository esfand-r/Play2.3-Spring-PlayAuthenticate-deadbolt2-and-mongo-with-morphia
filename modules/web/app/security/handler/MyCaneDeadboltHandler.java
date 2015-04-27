package security.handler;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.mycane.usermanagement.service.user.IUserService;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Based on and brought mainly untouched from <a href="https://github.com/joscha/play-authenticate">Joscha Feth's PlayAuthenticate</a>
 */
public class MyCaneDeadboltHandler extends AbstractDeadboltHandler {
    private IUserService userService;

    @Override
    public F.Promise<Result> beforeAuthCheck(final Http.Context context) {
        if (PlayAuthenticate.isLoggedIn(context.session())) {
            // user is logged in
            return F.Promise.pure(null);
        } else {
            // user is not logged in

            // call this if you want to redirect your visitor to the page that
            // was requested before sending him to the login page
            // if you don't call this, the user will get redirected to the page
            // defined by your resolver
            final String originalUrl = PlayAuthenticate.storeOriginalUrl(context);

            context.flash().put("error", "You need to log in first, to view '" + originalUrl + "'");
            return F.Promise.promise(() -> redirect(PlayAuthenticate.getResolver().login()));
        }
    }

    @Override
    public Subject getSubject(final Http.Context context) {
        final AuthUserIdentity u = PlayAuthenticate.getUser(context);
        return userService.findByAuthUserIdentity(u);
    }

    @Override
    public DynamicResourceHandler getDynamicResourceHandler(final Http.Context context) {
        return null;
    }

    @Override
    public F.Promise<Result> onAuthFailure(final Http.Context context, final String content) {
        // if the user has a cookie with a valid user and the local user has
        // been deactivated/deleted in between, it is possible that this gets
        // shown. You might want to consider to sign the user out in this case.
        return F.Promise.promise(() -> forbidden("Forbidden"));
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}
