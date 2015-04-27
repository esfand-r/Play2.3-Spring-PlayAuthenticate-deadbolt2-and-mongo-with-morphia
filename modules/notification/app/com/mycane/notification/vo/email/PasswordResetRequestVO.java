package com.mycane.notification.vo.email;

import com.mycane.security.model.usermanagement.User;
import play.i18n.Lang;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by esfandiaramirrahimi on 2015-05-09.
 */
public class PasswordResetRequestVO {
    private String url;
    private User user;
    private Lang lang;
    private List<Lang> acceptLanguages;
    private String token;

    private PasswordResetRequestVO(final Builder builder) {
        url = builder.url;
        lang = builder.lang;
        acceptLanguages = builder.acceptLanguages;
        token = builder.token;
        user = builder.user;
    }

    public static final class Builder {
        private String url;
        private Lang lang;
        private List<Lang> acceptLanguages;
        private String token;
        private User user;

        public Builder() {
        }

        @NotNull
        public Builder withUrl(@NotNull final String url) {
            this.url = url;
            return this;
        }

        public Builder withLang(final Lang lang) {
            this.lang = lang;
            return this;
        }

        public Builder withAcceptLanguages(final List<Lang> acceptLanguages) {
            this.acceptLanguages = acceptLanguages;
            return this;
        }

        @NotNull
        public Builder withToken(@NotNull final String token) {
            this.token = token;
            return this;
        }

        @NotNull
        public Builder withUser(final User user) {
            this.user = user;
            return this;
        }

        @NotNull
        public PasswordResetRequestVO build() {
            return new PasswordResetRequestVO(this);
        }
    }

    public String getUrl() {
        return url;
    }


    public Lang getLang() {
        return lang;
    }

    public List<Lang> getAcceptLanguages() {
        return acceptLanguages;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
