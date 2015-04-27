package com.mycane.usermanagement.service.account;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.mycane.security.model.usermanagement.LinkedAccount;
import com.mycane.security.model.usermanagement.TokenAction;
import com.mycane.security.model.usermanagement.User;
import com.mycane.usermanagement.dao.UserDao;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.UUID;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Named
public class AccountService implements IAccountService {
    private final static long VERIFICATION_TIME = 7 * 24 * 3600;

    @Inject
    private TokenService tokenService;
    @Inject
    private UserDao userDao;

    private TokenAction createTokenAction(final TokenAction.Type type, final String token, final User targetUser) {
        final TokenAction tokenAction = new TokenAction();
        tokenAction.setTargetUser(targetUser);
        tokenAction.setToken(token);
        tokenAction.setType(type);
        final Date created = new Date();
        tokenAction.setCreated(created);
        tokenAction.setExpires(new Date(created.getTime() + VERIFICATION_TIME * 1000));
        tokenService.create(tokenAction);
        return tokenAction;
    }

    @Override
    public void verify(User unverified) {
        unverified.setEmailValidated(true);
        userDao.save(unverified);
        tokenService.deleteByUser(unverified, TokenAction.Type.EMAIL_VERIFICATION);
    }

    @Override
    public void setLastLoginDate(AuthUser knownUser) {
        final User user = userDao.findByAuthUserIdentity(knownUser);
        user.setLastLogin(new Date());
        userDao.save(user);
    }

    @Override
    public void addLinkedAccount(AuthUser oldUser, AuthUser newUser) {
        final User user = userDao.findByAuthUserIdentity(oldUser);
        user.getLinkedAccounts().add(LinkedAccount.create(newUser));
        userDao.save(user);
    }

    @Override
    public void changePassword(User localUser, UsernamePasswordAuthUser authUser, boolean create) {
        LinkedAccount linkedAccount = localUser.getAccountByProvider(authUser.getProvider());
        if (linkedAccount == null) {
            if (create) {
                linkedAccount = LinkedAccount.create(authUser);
            } else {
                throw new RuntimeException("Account not enabled for password usage");
            }
        }
        linkedAccount.setProviderUserId(authUser.getHashedPassword());
        localUser.getLinkedAccounts().add(linkedAccount);
        userDao.save(localUser);
    }

    @Override
    public void resetPassword(User localUser, UsernamePasswordAuthUser authUser, boolean create) {
        changePassword(localUser, authUser, create);
        tokenService.deleteByUser(localUser, TokenAction.Type.PASSWORD_RESET);
    }

    @Override
    public void merge(AuthUser oldUser, AuthUser newUser) {
        merge(userDao.findByAuthUserIdentity(oldUser), userDao.findByAuthUserIdentity(newUser));
    }

    @Override
    public void merge(User localUser, User otherUser) {
        for (final LinkedAccount acc : localUser.getLinkedAccounts()) {
            localUser.getLinkedAccounts().add(LinkedAccount.create(acc));
        }
        // do all other merging stuff here - like resources, etc.
        // deactivate the merged user that got added to this one
        localUser.setActive(false);
        userDao.save(otherUser);
        userDao.save(localUser);
    }

    @Override
    public String generateVerificationRecord(final User user) {
        final String token = generateToken();
        // Do database actions, etc.
        createTokenAction(TokenAction.Type.EMAIL_VERIFICATION, token, user);
        return token;
    }

    //todo: create token generator service.
    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
