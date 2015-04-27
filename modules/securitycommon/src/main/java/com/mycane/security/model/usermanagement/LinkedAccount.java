package com.mycane.security.model.usermanagement;

import com.feth.play.module.pa.user.AuthUser;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.List;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Entity
public class LinkedAccount implements Serializable {
    @Id
    private ObjectId id;

    private String providerUserId;

    private String providerKey;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public static LinkedAccount findByProviderKey(final List<LinkedAccount> accounts, String key) {
        for (LinkedAccount linkedAccount : accounts) {
            if (linkedAccount.providerKey.equalsIgnoreCase(key)) {
                return linkedAccount;
            }
        }
        return null;
    }

    public static LinkedAccount create(final AuthUser authUser) {
        final LinkedAccount linkedAccount = new LinkedAccount();
        linkedAccount.update(authUser);
        return linkedAccount;
    }

    public void update(final AuthUser authUser) {
        this.providerKey = authUser.getProvider();
        this.providerUserId = authUser.getId();
    }

    public static LinkedAccount create(final LinkedAccount acc) {
        final LinkedAccount linkedAccount = new LinkedAccount();
        linkedAccount.providerKey = acc.providerKey;
        linkedAccount.providerUserId = acc.providerUserId;
        return linkedAccount;
    }
}
