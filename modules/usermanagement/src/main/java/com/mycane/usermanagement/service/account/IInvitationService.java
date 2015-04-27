package com.mycane.usermanagement.service.account;

import com.mycane.security.model.usermanagement.Invitation;

/**
 * Created by esfandiaramirrahimi on 2015-05-04.
 */
public interface IInvitationService {
    public Invitation create(final String invitorEmail, final String inviteeEmail);
}
