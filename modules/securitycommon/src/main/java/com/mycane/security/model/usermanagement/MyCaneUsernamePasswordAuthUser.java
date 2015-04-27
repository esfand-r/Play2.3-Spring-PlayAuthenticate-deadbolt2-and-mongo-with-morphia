package com.mycane.security.model.usermanagement;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.NameIdentity;


/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public class MyCaneUsernamePasswordAuthUser extends UsernamePasswordAuthUser implements NameIdentity {
    private static final long serialVersionUID = 1L;

    private final String name;

    public MyCaneUsernamePasswordAuthUser(final String signupName, final String password, final String email) {
        super(password, email);
        this.name = signupName;
    }

    /**
     * Used for password reset only - do not use this to signup a user!
     */
    public MyCaneUsernamePasswordAuthUser(final String password) {
        super(password, null);
        name = null;
    }

    @Override
    public String getName() {
        return name;
    }
}
