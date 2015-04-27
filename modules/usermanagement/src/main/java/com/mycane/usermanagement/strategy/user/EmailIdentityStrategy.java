package com.mycane.usermanagement.strategy.user;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.EmailIdentity;
import com.mycane.security.model.usermanagement.User;

import javax.inject.Named;

/**
 * Created by esfandiaramirrahimi on 2015-05-07.
 */
@Named
@IUserStrategy(type = IUserIdentityStrategy.class, clazz = EmailIdentity.class)
public class EmailIdentityStrategy implements IUserIdentityStrategy {
    @Override
    public void apply(final AuthUser authUser, final User user) {
        final EmailIdentity identity = (EmailIdentity) authUser;
        user.setEmail(identity.getEmail());
        user.setEmailValidated(false);
    }
}
