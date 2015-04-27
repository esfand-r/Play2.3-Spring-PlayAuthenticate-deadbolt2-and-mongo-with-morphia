package forms.usermanagement;

import play.data.validation.Constraints;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 */
public class LoginForm extends IdentityForm implements com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.UsernamePassword {
    @Constraints.Required
    @Constraints.MinLength(5)
    private String password;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
