package com.mycane.security.model.usermanagement;

import be.objectify.deadbolt.core.models.Permission;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.List;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Entity
public class UserPermission implements Permission, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId id;

    private String value;

    public String getValue() {
        return value;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static UserPermission findByValue(List<UserPermission> userPermissions, String value) {
        for (UserPermission permission : userPermissions) {
            if (permission.value.equalsIgnoreCase(value))
                return permission;
        }
        return null;
    }
}
