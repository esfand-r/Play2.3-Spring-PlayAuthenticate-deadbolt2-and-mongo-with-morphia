package security.plugin;

import com.feth.play.module.pa.service.UserServicePlugin;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.mycane.security.model.usermanagement.User;
import com.mycane.usermanagement.service.account.IAccountService;
import com.mycane.usermanagement.service.user.IUserService;
import play.Application;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public class MyCaneUserServicePlugin extends UserServicePlugin {
    private IAccountService accountService;
    private IUserService userService;

    public MyCaneUserServicePlugin(final Application app, final IUserService userService, final IAccountService accountService) {
        super(app);
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public Object save(final AuthUser authUser) {
        final boolean isLinked = userService.existsByAuthUserIdentity(authUser);
        if (!isLinked) {
            return userService.create(authUser).getId();
        } else {
            // we have this user already, so return null
            return null;
        }
    }

    @Override
    public Object getLocalIdentity(final AuthUserIdentity identity) {
        final User localUser = userService.findByAuthUserIdentity(identity);
        if (localUser != null) {
            return localUser.getId();
        } else {
            return null;
        }
    }

    @Override
    public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
        if (!oldUser.equals(newUser)) {
            accountService.merge(oldUser, newUser);
        }
        return oldUser;
    }

    @Override
    public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
        accountService.addLinkedAccount(oldUser, newUser);
        return newUser;
    }

    @Override
    public AuthUser update(final AuthUser knownUser) {
        // User logged in again, bump last login date
        accountService.setLastLoginDate(knownUser);
        return knownUser;
    }
}
