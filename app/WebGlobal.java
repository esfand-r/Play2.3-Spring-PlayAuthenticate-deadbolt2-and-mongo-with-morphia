import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;
import com.mycane.SecurityCommonConfig;
import com.mycane.notification.NotificationConfig;
import com.mycane.security.model.usermanagement.SecurityRole;
import com.mycane.usermanagement.UserManagementConfig;
import com.mycane.usermanagement.type.RoleType;
import controllers.routes;
import org.mongodb.morphia.Datastore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Call;

import javax.inject.Named;
import java.util.Arrays;

/**
 * Created by esfandiaramirrahimi on 2015-05-09.
 */
public class WebGlobal extends GlobalSettings {
    private static AnnotationConfigApplicationContext applicationContext;

    public void onStart(Application app) {
        Logger.info("Application started!");

        applicationContext = new AnnotationConfigApplicationContext(
                SecurityCommonConfig.class,
                UserManagementConfig.class,
                NotificationConfig.class,
                WebConfig.class);

        PlayAuthenticate.setResolver(new PlayAuthenticate.Resolver() {
            @Override
            public Call login() {
                return routes.AuthenticationController.login();
            }

            @Override
            public Call afterAuth() {
                return routes.ApplicationController.index();
            }

            @Override
            public Call afterLogout() {
                return routes.ApplicationController.index();
            }

            @Override
            public Call auth(final String provider) {
                // Avoided using the default to have more control here .
                return routes.AuthenticationController.authenticate(provider);
            }

            @Override
            public Call askMerge() {
                return routes.UserAccountController.askMerge();
            }

            @Override
            public Call askLink() {
                return routes.UserAccountController.askLink();
            }

            @Override
            public Call onException(final AuthException e) {
                if (e instanceof AccessDeniedException) {
                    return routes.AuthenticationController.oAuthDenied(((AccessDeniedException) e).getProviderKey());
                }
                return super.onException(e);
            }
        });

        initialData();
    }

    public static ApplicationContext getContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("application context is not initialized");
        }
        return applicationContext;
    }

    @Override
    public <A> A getControllerInstance(Class<A> clazz) throws Exception {
        if (clazz.getAnnotation(Named.class) != null) {
            return applicationContext.getBean(clazz);
        } else {
            return super.getControllerInstance(clazz);
        }
    }

    public void onStop(Application app) {
        Logger.info("Appplication stopped!");
        if (applicationContext != null) {
            applicationContext.close();
        } else {
            Logger.info("Spring application Context Was Never Loaded.");
        }
    }

    // Creating first roles.
    private void initialData() {
        final Datastore datastore = applicationContext.getBean("datastore", Datastore.class);
        if (datastore.createQuery(SecurityRole.class).countAll() == 0) {
            for (final String roleName : Arrays.asList(RoleType.USER)) {
                final SecurityRole role = new SecurityRole();
                role.setRoleName(roleName);
                datastore.save(role);
            }
        }
    }
}
