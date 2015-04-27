package forms.usermanagement;

import play.data.format.Formats;
import play.data.validation.Constraints;

/**
 * Created by esfandiaramirrahimi on 2015-05-02.
 */
public class AcceptForm {
    @Constraints.Required
    @Formats.NonEmpty
    private Boolean accept;

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }
}
