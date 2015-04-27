package com.mycane.usermanagement.service.account;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.mycane.security.model.usermanagement.User;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public interface IAccountService {
    String generateVerificationRecord(final User user);

    void verify(final User unverified);

    void setLastLoginDate(final AuthUser knownUser);

    void addLinkedAccount(final AuthUser oldUser, final AuthUser newUser);

    void changePassword(final User localUser, final UsernamePasswordAuthUser authUser, final boolean create);

    void resetPassword(final User localUser, final UsernamePasswordAuthUser authUser, final boolean create);

    void merge(final AuthUser oldUser, final AuthUser newUser);

    void merge(final User localUSer, final User otherUser);
}
