package com.mycane.notification;

import com.feth.play.module.mail.Mailer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@ComponentScan(basePackages = {"com.mycane.notification.service"})
@Configuration
public class NotificationConfig {
    @Bean
    public Mailer mailer() {
        return Mailer.getDefaultMailer();
    }
}
