package forms.usermanagement;

import play.data.validation.Constraints;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 */
public class IdentityForm {
    public IdentityForm() {
    }

    public IdentityForm(final String email) {
        this.email = email;
    }

    @Constraints.Required
    @Constraints.Email
    protected String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
