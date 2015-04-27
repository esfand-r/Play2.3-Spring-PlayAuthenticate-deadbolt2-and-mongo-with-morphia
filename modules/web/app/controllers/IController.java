package controllers;

import play.Play;

/**
 * Created by esfandiaramirrahimi on 2015-05-08.
 */
public interface IController {
    String HTTPS_SETTING = "play-authenticate.password.mail.verificationLink.secure";
    boolean isSecure = Play.application().configuration().getBoolean(HTTPS_SETTING);
    String FLASH_MESSAGE_KEY = "message";
    String FLASH_ERROR_KEY = "error";
}
