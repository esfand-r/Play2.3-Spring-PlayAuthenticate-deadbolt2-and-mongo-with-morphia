package com.mycane.usermanagement.service.account;

import com.mycane.security.model.usermanagement.TokenAction;
import com.mycane.security.model.usermanagement.User;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public interface ITokenService {
    TokenAction findByToken(final String token, final TokenAction.Type type);

    void create(final TokenAction tokenAction);

    TokenAction create(final TokenAction.Type type, final User targetUser);

    void deleteByUser(final User u, final TokenAction.Type type);
}
