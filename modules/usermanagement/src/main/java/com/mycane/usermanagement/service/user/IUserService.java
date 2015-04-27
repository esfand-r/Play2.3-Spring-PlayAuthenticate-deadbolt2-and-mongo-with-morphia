package com.mycane.usermanagement.service.user;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.mycane.security.model.usermanagement.SecurityRole;
import com.mycane.security.model.usermanagement.User;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public interface IUserService {
    public User create(final AuthUser authUser);

    User getLocalUser(final AuthUser authUser);

    boolean existsByAuthUserIdentity(final AuthUserIdentity identity);

    User findByUsernamePasswordIdentity(final UsernamePasswordAuthUser identity);

    User findByAuthUserIdentity(final AuthUserIdentity identity);

    User findByEmail(final String email);

    SecurityRole findByRoleName(final String roleName);
}
