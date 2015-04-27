package com.mycane.usermanagement.dao;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.mycane.security.model.usermanagement.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Repository
public class UserDao extends BasicDAO<User, ObjectId> {
    @Inject
    public UserDao(final Datastore datastore) {
        super(datastore);
    }

    public User findByAuthUserIdentity(final AuthUserIdentity identity) {
        if (identity == null) {
            return null;
        }
        if (identity instanceof UsernamePasswordAuthUser) {
            return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
        } else {
            return getAuthUserFind(identity).get();
        }
    }

    public User findByUsernamePasswordIdentity(final UsernamePasswordAuthUser identity) {
        return getUsernamePasswordAuthUserFind(identity);
    }

    public User getUsernamePasswordAuthUserFind(final UsernamePasswordAuthUser identity) {
        return getEmailUserFind(identity).get();
    }

    public Query<User> getEmailUserFind(final String email) {
        return createQuery().filter("active", true)
                .filter("email", email);
    }

    public Query<User> getAuthUserFind(final AuthUserIdentity identity) {
        return createQuery().filter("active =", true)
                .filter("linkedAccounts.providerUserId", identity.getId())
                .filter("linkedAccounts.providerKey", identity.getProvider());
    }

    public boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {
        final Query<User> exp;
        if (identity instanceof UsernamePasswordAuthUser) {
            exp = getEmailUserFind((UsernamePasswordAuthUser) identity);
        } else {
            exp = getAuthUserFind(identity);
        }
        return exp.countAll() > 0;
    }

    private Query<User> getEmailUserFind(final UsernamePasswordAuthUser identity) {
        return getEmailUserFind(identity.getEmail()).filter("linkedAccounts.providerKey", identity.getProvider());
    }
}
