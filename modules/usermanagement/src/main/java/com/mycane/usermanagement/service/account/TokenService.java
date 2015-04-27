package com.mycane.usermanagement.service.account;

import com.mycane.security.model.usermanagement.TokenAction;
import com.mycane.security.model.usermanagement.User;
import com.mycane.usermanagement.dao.TokenDao;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.UUID;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Named
public class TokenService implements ITokenService {
    /**
     * Verification time frame (until the user clicks on the link in the email)
     * in seconds
     * Defaults to one week
     */
    private final static long VERIFICATION_TIME = 7 * 24 * 3600;

    @Inject
    private TokenDao tokenDao;

    @Override
    public TokenAction findByToken(final String token, final TokenAction.Type type) {
        return tokenDao.findByToken(token, type);
    }

    @Override
    public void create(final TokenAction tokenAction) {
        tokenDao.save(tokenAction);
    }

    @Override
    public TokenAction create(final TokenAction.Type type, final User targetUser) {
        final TokenAction tokenAction = new TokenAction();
        tokenAction.setTargetUser(targetUser);
        tokenAction.setToken(generateToken());
        tokenAction.setType(type);
        final Date created = new Date();
        tokenAction.setCreated(created);
        tokenAction.setExpires(new Date(created.getTime() + VERIFICATION_TIME * 1000));
        tokenDao.save(tokenAction);
        return tokenAction;
    }

    @Override
    public void deleteByUser(final User user, final TokenAction.Type type) {
        tokenDao.delete(tokenDao.findTokenByUserAndType(user, type));
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
