package com.mycane.usermanagement.strategy.user;

import com.feth.play.module.pa.user.AuthUser;
import com.mycane.security.model.usermanagement.User;

/**
 * Created by esfandiaramirrahimi on 2015-05-07.
 */
public interface IUserIdentityStrategy {
    void apply(final AuthUser authUser, final User user);
}
