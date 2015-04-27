package com.mycane.usermanagement.dao;

import com.mycane.security.model.usermanagement.TokenAction;
import com.mycane.security.model.usermanagement.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Repository
public class TokenDao extends BasicDAO<TokenAction, ObjectId> {
    @Inject
    public TokenDao(final Datastore datastore) {
        super(datastore);
    }

    public TokenAction findTokenByUserAndType(final User user, final TokenAction.Type type) {
        return createQuery().filter("targetUser", user).filter("type", type).get();
    }

    public TokenAction findByToken(final String token, final TokenAction.Type type) {
        return createQuery()
                .filter("token", token)
                .filter("type", type)
                .get();
    }
}
