package forms.usermanagement;

import play.data.validation.Constraints;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 */
public class InvitationForm {
    @Valid
    private List<InvitePair> invites;

    public static class InvitePair {
        private String name;
        @Constraints.Email
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public List<InvitePair> getInvites() {
        return invites;
    }

    public void setInvites(List<InvitePair> invites) {
        this.invites = invites;
    }
}
