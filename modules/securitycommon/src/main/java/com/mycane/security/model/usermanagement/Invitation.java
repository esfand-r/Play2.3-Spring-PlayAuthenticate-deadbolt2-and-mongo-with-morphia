package com.mycane.security.model.usermanagement;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Entity
public class Invitation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId id;

    @Column
    private String invitorEmail;

    @Column(unique = true)
    private String inviteeEmail;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getInvitorEmail() {
        return invitorEmail;
    }

    public void setInvitorEmail(String invitorEmail) {
        this.invitorEmail = invitorEmail;
    }

    public String getInviteeEmail() {
        return inviteeEmail;
    }

    public void setInviteeEmail(String inviteeEmail) {
        this.inviteeEmail = inviteeEmail;
    }
}
