package com.mycane.usermanagement.strategy.user;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * Created by esfandiaramirrahimi on 2015-05-07.
 */
@Named
public class UserStrategyContainer {
    @Inject
    private ApplicationContext applicationContext;

    private final Map<Class, List<Object>> annotatedTypes = new HashMap<>();
    private final Map<Class, IUserStrategy> userStrategyCache = new HashMap<>();

    @PostConstruct
    public void init() {
        final Map<String, Object> beansWithUserStrategyAnnotation = applicationContext.getBeansWithAnnotation(IUserStrategy.class);

        initialize(beansWithUserStrategyAnnotation.values());

        for (final Object userStrategyBean : beansWithUserStrategyAnnotation.values()) {
            final IUserStrategy userStrategy = userStrategyCache.get(userStrategyBean.getClass());
            getBeansWithSameType(userStrategy).add(userStrategyBean);
        }
    }

    private void initialize(Collection<Object> annotatedBeanClasses) {
        for (final Object userStrategy : annotatedBeanClasses) {
            final IUserStrategy strategyAnnotation = AnnotationUtils.findAnnotation(userStrategy.getClass(), IUserStrategy.class);
            userStrategyCache.put(userStrategy.getClass(), strategyAnnotation);
        }
    }

    private List<Object> getBeansWithSameType(final IUserStrategy strategyAnnotation) {
        final List<Object> beansWithSameType = annotatedTypes.get(strategyAnnotation.type());
        if (beansWithSameType != null) {
            return beansWithSameType;
        } else {
            final List<Object> newBeansList = new ArrayList<>();
            annotatedTypes.put(strategyAnnotation.type(), newBeansList);
            return newBeansList;
        }
    }

    public <T> List<T> getStrategy(final Class<T> strategyType, final Class currentStrategy) {
        final List<Object> strategyBeans = annotatedTypes.get(strategyType);
        Assert.notEmpty(strategyBeans, "No strategies found of type '" + strategyType.getName() + "', are the strategies marked with @Strategy?");

        final List<T> strategies = new ArrayList<>();

        for (final Object userStrategy : strategyBeans) {
            final IUserStrategy strategyAnnotation = userStrategyCache.get(userStrategy.getClass());
            final Class strategyInAnnotation = strategyAnnotation.clazz();

            if (currentStrategy != null && strategyInAnnotation.isAssignableFrom(currentStrategy)) {
                final T strategyImplementation = (T) userStrategy;
                strategies.add(strategyImplementation);
            }
        }
        if (strategies.isEmpty()) {
            throw new RuntimeException("No strategy found for this type of user : '" + strategyType + "'");
        }
        return strategies;
    }
}
