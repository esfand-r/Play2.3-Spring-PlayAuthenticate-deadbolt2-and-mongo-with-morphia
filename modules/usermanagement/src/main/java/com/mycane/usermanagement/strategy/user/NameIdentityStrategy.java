package com.mycane.usermanagement.strategy.user;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.NameIdentity;
import com.mycane.security.model.usermanagement.User;

import javax.inject.Named;

/**
 * Created by esfandiaramirrahimi on 2015-05-07.
 */
@Named
@IUserStrategy(type = IUserIdentityStrategy.class, clazz = NameIdentity.class)
public class NameIdentityStrategy implements IUserIdentityStrategy {
    @Override
    public void apply(final AuthUser authUser, final User user) {
        final NameIdentity identity = (NameIdentity) authUser;
        final String name = identity.getName();
        if (name != null) {
            user.setName(name);
        }
    }
}
