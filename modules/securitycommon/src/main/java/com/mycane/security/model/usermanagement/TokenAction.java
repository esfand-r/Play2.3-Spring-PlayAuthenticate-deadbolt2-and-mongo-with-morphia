package com.mycane.security.model.usermanagement;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;
import play.data.format.Formats;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Entity
public class TokenAction implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type {
        EMAIL_VERIFICATION,
        PASSWORD_RESET
    }

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String token;

    @Reference
    private User targetUser;

    private Type type;

    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expires;

    public boolean isValid() {
        return this.expires.after(new Date());
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
