package forms.usermanagement;

import play.data.validation.Constraints;
import play.i18n.Messages;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 */
public class SignupForm extends LoginForm {
    @Constraints.Required
    @Constraints.MinLength(5)
    private String repeatPassword;

    @Constraints.Required
    private String name;

    public String validate() {
        if (getPassword() == null || !getPassword().equals(repeatPassword)) {
            return Messages.get("mycane.password.signup.error.passwords_not_same");
        }
        return null;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
