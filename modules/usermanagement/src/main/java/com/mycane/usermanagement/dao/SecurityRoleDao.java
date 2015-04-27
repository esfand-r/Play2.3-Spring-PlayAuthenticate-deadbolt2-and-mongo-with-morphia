package com.mycane.usermanagement.dao;

import com.mycane.security.model.usermanagement.SecurityRole;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Repository
public class SecurityRoleDao extends BasicDAO<SecurityRole, ObjectId> {
    @Inject
    public SecurityRoleDao(final Datastore datastore) {
        super(datastore);
    }

    public SecurityRole findByRoleName(final String roleName) {
        return createQuery().filter("roleName =", roleName).get();
    }
}
