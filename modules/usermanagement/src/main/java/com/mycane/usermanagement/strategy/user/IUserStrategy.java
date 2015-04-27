package com.mycane.usermanagement.strategy.user;

import java.lang.annotation.*;

/**
 * Created by esfandiaramirrahimi on 2015-05-07.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IUserStrategy {
    Class type();

    Class clazz();
}
