import be.objectify.deadbolt.java.utils.PluginUtils;
import com.feth.play.module.mail.Mailer;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.AuthProvider;
import com.feth.play.module.pa.service.UserServicePlugin;
import com.mycane.notification.service.email.IEmailService;
import com.mycane.usermanagement.service.account.IAccountService;
import com.mycane.usermanagement.service.account.ITokenService;
import com.mycane.usermanagement.service.user.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import play.Logger;
import play.Play;
import providers.MyCaneUsernamePasswordAuthProvider;
import security.handler.MyCaneDeadboltHandler;
import security.plugin.MyCaneUserServicePlugin;

import javax.inject.Inject;

/**
 * Created by esfandiaramirrahimi on 2015-05-03.
 */
@ComponentScan(basePackages = {"service", "controllers"})
@Configuration
public class WebConfig {
    @Inject
    private ITokenService tokenService;
    @Inject
    private IUserService userService;
    @Inject
    private IEmailService emailService;
    @Inject
    private IAccountService accountService;

    // This is made to springify MyUsernamePasswordAuthProvider used with play authenticate.
    // Instead of using plugin mechanism(play.plugins). todo: check if exists?
    @Bean
    public AuthProvider myUsernamePasswordAuthProvider() {
        AuthProvider.Registry.unregister("password");
        MyCaneUsernamePasswordAuthProvider myCaneUsernamePasswordAuthProvider;
        myCaneUsernamePasswordAuthProvider = new MyCaneUsernamePasswordAuthProvider(Play.application());
        myCaneUsernamePasswordAuthProvider.setUserService(userService);
        myCaneUsernamePasswordAuthProvider.setMailer(Mailer.getDefaultMailer());
        myCaneUsernamePasswordAuthProvider.setTokenService(tokenService);
        myCaneUsernamePasswordAuthProvider.setEmailService(emailService);
        AuthProvider.Registry.register("password", myCaneUsernamePasswordAuthProvider);

        return myCaneUsernamePasswordAuthProvider;
    }

    @Bean
    public UserServicePlugin userServicePlugin() {
        MyCaneUserServicePlugin myCaneUserServicePlugin = new MyCaneUserServicePlugin(Play.application(), userService, accountService);
        PlayAuthenticate.setUserService(myCaneUserServicePlugin);
        return myCaneUserServicePlugin;
    }

    @Bean
    public MyCaneDeadboltHandler deadBoltHandler() {
        MyCaneDeadboltHandler myCaneDeadboltHandler = null;
        try {
            myCaneDeadboltHandler = (MyCaneDeadboltHandler) PluginUtils.getDeadboltHandler();
            myCaneDeadboltHandler.setUserService(userService);
        } catch (Exception e) {
            Logger.info("Could not get Deadbolt handler from PlayAuthenticate", e);
        }
        return myCaneDeadboltHandler;
    }
}
