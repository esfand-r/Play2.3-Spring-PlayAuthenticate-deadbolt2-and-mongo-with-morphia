package com.mycane.usermanagement.strategy.user;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.FirstLastNameIdentity;
import com.mycane.security.model.usermanagement.User;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Named;

/**
 * Created by esfandiaramirrahimi on 2015-05-07.
 */
@Named
@IUserStrategy(type = IUserIdentityStrategy.class, clazz = FirstLastNameIdentity.class)
public class FirstLastNameIdentityStrategy implements IUserIdentityStrategy {
    @Override
    public void apply(final AuthUser authUser, final User user) {
        final FirstLastNameIdentity identity = (FirstLastNameIdentity) authUser;
        final String firstName = identity.getFirstName();
        final String lastName = identity.getLastName();
        if (StringUtils.isNotBlank(firstName)) {
            user.setFirstName(firstName);
        }
        if (StringUtils.isNotBlank(lastName)) {
            user.setLastName(lastName);
        }
    }
}
