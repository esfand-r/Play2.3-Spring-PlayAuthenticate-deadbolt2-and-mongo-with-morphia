package com.mycane.notification.vo.email;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public class EmailTemplateParametersVO {
    private String templateUrl;
    private String langCode;
    private Map<String, String> customParameters = new TreeMap<>();

    private EmailTemplateParametersVO(final Builder builder) {
        templateUrl = builder.templateUrl;
        langCode = builder.langCode;
        customParameters = builder.customParameters;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public String getLangCode() {
        return langCode;
    }

    public Map<String, String> getCustomParameters() {
        return customParameters;
    }

    public static final class Builder {
        private String templateUrl;
        private String langCode;
        private Map<String, String> customParameters = new TreeMap<>();

        public Builder() {
        }

        public Builder withTemplateUrl(String templateUrl) {
            this.templateUrl = templateUrl;
            return this;
        }

        public Builder withLangCode(String langCode) {
            this.langCode = langCode;
            return this;
        }

        public Builder withCustomParameter(String key, String value) {
            this.customParameters.put(key, value);
            return this;
        }

        public EmailTemplateParametersVO build() {
            return new EmailTemplateParametersVO(this);
        }
    }
}
