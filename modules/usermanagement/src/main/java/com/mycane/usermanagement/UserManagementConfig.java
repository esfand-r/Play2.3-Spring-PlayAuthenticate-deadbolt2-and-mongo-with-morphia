package com.mycane.usermanagement;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@Configuration
@ComponentScan(basePackages = {"com.mycane.usermanagement.dao", "com.mycane.usermanagement.service",
        "com.mycane.usermanagement.security", "com.mycane.usermanagement.strategy"})
@Import({DBConfig.class, MorphiaConfig.class})
public class UserManagementConfig {
}
