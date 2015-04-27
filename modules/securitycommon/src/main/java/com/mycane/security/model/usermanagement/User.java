package com.mycane.security.model.usermanagement;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Entity
public class User implements Subject, Serializable {
    private static final long serialVersionUID = 1750161556629682356L;

    @Id
    private ObjectId id;
    @Constraints.Email
    // This has to be unique, hence users using different providers are forced to link their accounts.
    @Column(unique = true)
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;
    private boolean active;
    private boolean emailValidated;

    @Embedded
    private List<SecurityRole> roles = new ArrayList<>();

    @Embedded
    private List<LinkedAccount> linkedAccounts = new ArrayList<>();

    @Embedded
    private List<UserPermission> permissions = new ArrayList<>();

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    public void setRoles(List<SecurityRole> roles) {
        this.roles = roles;
    }

    public void setLinkedAccounts(List<LinkedAccount> linkedAccounts) {
        this.linkedAccounts = linkedAccounts;
    }

    public void setPermissions(List<UserPermission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getIdentifier() {
        return id.toString();
    }

    @Override
    public List<? extends Role> getRoles() {
        return roles;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return permissions;
    }

    public List<UserPermission> getUserPermissions() {
        return permissions;
    }

    public List<LinkedAccount> getLinkedAccounts() {
        return linkedAccounts;
    }

    public Set<String> getProviders() {
        final Set<String> providerKeys = new HashSet<>(linkedAccounts.size());
        providerKeys.addAll(linkedAccounts.stream().
                map(LinkedAccount::getProviderKey).
                collect(Collectors.toList()));
        return providerKeys;
    }

    public LinkedAccount getAccountByProvider(final String providerKey) {
        return LinkedAccount.findByProviderKey(this.linkedAccounts, providerKey);
    }
}
