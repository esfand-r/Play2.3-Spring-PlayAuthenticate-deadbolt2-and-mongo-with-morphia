package forms.usermanagement;

import play.data.validation.Constraints;
import play.i18n.Messages;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 */
public class PasswordChangeForm {
    @Constraints.MinLength(5)
    @Constraints.Required
    private String password;

    @Constraints.MinLength(5)
    @Constraints.Required
    private String repeatPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String validate() {
        if (password == null || !password.equals(repeatPassword)) {
            return Messages
                    .get("mycane.change_password.error.passwords_not_same");
        }
        return null;
    }
}
