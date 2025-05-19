package com.github.yannicklamprecht.springframework.extensions.logging;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.test.appender.ListAppender;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class Log4jTestExtension implements BeforeEachCallback, AfterAllCallback, TestInstancePostProcessor {

    private static final String APPENDER_NAME = "listAppender";

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        rootLogger.getContext().reconfigure();
        Configuration configuration = rootLogger.getContext().getConfiguration();


        var listAppender = new ListAppender(APPENDER_NAME);
        listAppender.start();
        configuration.addAppender(listAppender);
        rootLogger.addAppender(listAppender);

        Arrays.stream(testInstance.getClass().getDeclaredFields()).filter(field -> field.getType() == ListAppender.class)
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(testInstance, listAppender);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        Configuration configuration = rootLogger.getContext().getConfiguration();


        // Remove and stop TestLogAppender
        Appender appender = rootLogger.getAppenders().get(APPENDER_NAME);
        rootLogger.removeAppender(appender);
        configuration.getAppenders().remove(APPENDER_NAME);
        appender.stop();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        ListAppender appender = (ListAppender) rootLogger.getAppenders().get(APPENDER_NAME);
        appender.clear();
    }
}
