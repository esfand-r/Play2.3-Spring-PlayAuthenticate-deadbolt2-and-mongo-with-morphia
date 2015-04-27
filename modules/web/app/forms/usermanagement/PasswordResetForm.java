package forms.usermanagement;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 */
public class PasswordResetForm extends PasswordChangeForm {
    private String token;

    public PasswordResetForm() {
    }

    public PasswordResetForm(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
