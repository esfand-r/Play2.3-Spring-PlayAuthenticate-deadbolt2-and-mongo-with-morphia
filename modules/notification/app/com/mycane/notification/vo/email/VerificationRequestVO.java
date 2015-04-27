package com.mycane.notification.vo.email;

import play.i18n.Lang;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by esfandiaramirrahimi on 2015-05-09.
 */
public class VerificationRequestVO {
    private String url;
    private String name;
    private String email;
    private Lang lang;
    private List<Lang> acceptLanguages;
    private String token;
    private boolean isAfterSignup;

    private VerificationRequestVO(final Builder builder) {
        url = builder.url;
        name = builder.name;
        email = builder.email;
        lang = builder.lang;
        acceptLanguages = builder.acceptLanguages;
        token = builder.token;
        isAfterSignup = builder.isAfterSignup;
    }

    public static final class Builder {
        private String url;
        private String name;
        private String email;
        private Lang lang;
        private List<Lang> acceptLanguages;
        private String token;
        private boolean isAfterSignup;

        public Builder() {
        }

        @NotNull
        public Builder withUrl(@NotNull final String url) {
            this.url = url;
            return this;
        }

        @NotNull
        public Builder withName(@NotNull final String name) {
            this.name = name;
            return this;
        }

        @NotNull
        public Builder withEmail(@NotNull final String email) {
            this.email = email;
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
        public Builder withIsAfterSignup(final boolean isAfterSignup) {
            this.isAfterSignup = isAfterSignup;
            return this;
        }

        @NotNull
        public VerificationRequestVO build() {
            return new VerificationRequestVO(this);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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

    public boolean isAfterSignup() {
        return isAfterSignup;
    }
}
