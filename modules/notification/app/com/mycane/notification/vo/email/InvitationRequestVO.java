package com.mycane.notification.vo.email;

import play.i18n.Lang;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by esfandiaramirrahimi on 2015-05-09.
 */
public class InvitationRequestVO {
    private String name;
    private String email;
    private String url;
    private Lang lang;
    private List<Lang> acceptLanguages;

    private InvitationRequestVO(final Builder builder) {
        url = builder.url;
        lang = builder.lang;
        name = builder.name;
        email = builder.email;
        acceptLanguages = builder.acceptLanguages;
    }

    public static final class Builder {
        private String url;
        private Lang lang;
        private String name;
        private String email;
        private List<Lang> acceptLanguages;

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
        public Builder withName(@NotNull final String name) {
            this.name = name;
            return this;
        }

        @NotNull
        public Builder withEmail(final String email) {
            this.email = email;
            return this;
        }

        @NotNull
        public InvitationRequestVO build() {
            return new InvitationRequestVO(this);
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

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
