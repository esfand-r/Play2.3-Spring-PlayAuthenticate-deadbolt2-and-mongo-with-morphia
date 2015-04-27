package com.mycane.security.model.usermanagement;

import com.feth.play.module.pa.providers.password.DefaultUsernamePasswordAuthUser;


/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public class MyCaneLoginUsernamePasswordAuthUser extends DefaultUsernamePasswordAuthUser {
    private static final long serialVersionUID = 1L;
    /**
     * The session timeout in seconds
     * Defaults to two weeks
     */
    final static long SESSION_TIMEOUT = 24 * 14 * 3600;
    private long expiration;

    /**
     * For logging the user in automatically
     */
    public MyCaneLoginUsernamePasswordAuthUser(final String email) {
        this(null, email);
    }

    public MyCaneLoginUsernamePasswordAuthUser(final String clearPassword,
                                               final String email) {
        super(clearPassword, email);

        expiration = System.currentTimeMillis() + 1000 * SESSION_TIMEOUT;
    }

    @Override
    public long expires() {
        return expiration;
    }

}
