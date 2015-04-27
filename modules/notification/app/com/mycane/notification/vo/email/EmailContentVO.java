package com.mycane.notification.vo.email;

import com.mycane.notification.vo.IMessageContent;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
public class EmailContentVO implements IMessageContent {
    private String html;
    private String text;

    private EmailContentVO(final Builder builder) {
        html = builder.html;
        text = builder.text;
    }

    @Override
    public String getHtml() {
        return html;
    }

    @Override
    public String getText() {
        return text;
    }

    public static final class Builder {
        private String html;
        private String text;

        public Builder() {
        }

        public Builder withHtml(String html) {
            this.html = html;
            return this;
        }

        public Builder withTxt(String txt) {
            this.text = txt;
            return this;
        }

        public EmailContentVO build() {
            return new EmailContentVO(this);
        }
    }
}
