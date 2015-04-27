package com.mycane.usermanagement;

import com.mycane.security.model.usermanagement.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * Created by esfandiaramirrahimi on 2015-05-09.
 */
@Configuration
public class MorphiaConfig {
    @Inject
    public void configureGlobal(Datastore datastore, Morphia morphia) throws Exception {
        morphia.map(User.class).
                map(LinkedAccount.class).
                map(SecurityRole.class).
                map(TokenAction.class).
                map(UserPermission.class).
                map(Invitation.class);

        datastore.ensureIndexes();
        datastore.ensureCaps();
    }

}
