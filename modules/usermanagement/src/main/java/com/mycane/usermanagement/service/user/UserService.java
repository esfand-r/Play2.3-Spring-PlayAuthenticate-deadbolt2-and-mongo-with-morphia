package com.mycane.usermanagement.service.user;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.mycane.security.model.usermanagement.LinkedAccount;
import com.mycane.security.model.usermanagement.SecurityRole;
import com.mycane.security.model.usermanagement.User;
import com.mycane.usermanagement.dao.SecurityRoleDao;
import com.mycane.usermanagement.dao.UserDao;
import com.mycane.usermanagement.strategy.user.IUserIdentityStrategy;
import com.mycane.usermanagement.strategy.user.UserStrategyContainer;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by esfandiaramirrahimi on 2015-05-05.
 */
@Named
public class UserService implements IUserService {
    private static final String USER_ROLE = "user";

    @Inject
    private UserDao userDao;
    @Inject
    private SecurityRoleDao securityRoleDao;
    @Inject
    private UserStrategyContainer userStrategyContainer;

    @Override
    public User create(final AuthUser authUser) {
        final User user = new User();
        user.setRoles(Collections.singletonList(findByRoleName(USER_ROLE)));
        user.setActive(true);
        user.setLastLogin(new Date());
        user.setLinkedAccounts(Collections.singletonList(LinkedAccount.create(authUser)));

        final List<IUserIdentityStrategy> strategies = userStrategyContainer.getStrategy(IUserIdentityStrategy.class, authUser.getClass());
        for (IUserIdentityStrategy userCreationStrategy : strategies) {
            userCreationStrategy.apply(authUser, user);
        }
        userDao.save(user);
        return user;
    }

    @Override
    public User getLocalUser(final AuthUser authUser) {
        return userDao.findByAuthUserIdentity(authUser);
    }

    @Override
    public boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {
        return userDao.existsByAuthUserIdentity(identity);
    }

    @Override
    public User findByUsernamePasswordIdentity(final UsernamePasswordAuthUser identity) {
        return userDao.getUsernamePasswordAuthUserFind(identity);
    }

    @Override
    public User findByAuthUserIdentity(final AuthUserIdentity identity) {
        return userDao.findByAuthUserIdentity(identity);
    }

    @Override
    public User findByEmail(final String email) {
        return userDao.getEmailUserFind(email).get();
    }

    @Override
    public SecurityRole findByRoleName(final String roleName) {
        return securityRoleDao.findByRoleName(roleName);
    }
}
