package com.mycane.usermanagement.service.account;

import com.mycane.security.model.usermanagement.Invitation;
import com.mycane.usermanagement.dao.InvitationDao;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by esfandiaramirrahimi on 2015-05-04.
 */
@Named
public class InvitationService implements IInvitationService {
    @Inject
    private InvitationDao invitationDao;

    @Override
    public Invitation create(final String invitorEmail, final String inviteeEmail) {
        final Invitation invitation = new Invitation();
        invitation.setInviteeEmail(inviteeEmail);
        invitation.setInvitorEmail(invitorEmail);
        invitationDao.save(invitation);
        return invitation;
    }
}
