package com.mycane.usermanagement.dao;

import com.mycane.security.model.usermanagement.Invitation;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * Created by esfandiaramirrahimi on 2015-05-06.
 */
@Repository
public class InvitationDao extends BasicDAO<Invitation, ObjectId> {
    @Inject
    public InvitationDao(final Datastore datastore) {
        super(datastore);
    }
}
