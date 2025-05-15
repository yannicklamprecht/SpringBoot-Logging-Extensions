package com.github.yannicklamprecht.springframework.extensions.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class Log4jTestExtension implements BeforeEachCallback, AfterEachCallback {

    private TestLogAppender testLogAppender;

    @Override
    public void beforeEach(ExtensionContext context) {
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        Configuration configuration = rootLogger.getContext().getConfiguration();

        // Create and add TestLogAppender
        testLogAppender = new TestLogAppender("TestLogAppender");
        testLogAppender.start();
        configuration.addAppender(testLogAppender);
        rootLogger.addAppender(testLogAppender);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        Configuration configuration = rootLogger.getContext().getConfiguration();

        // Remove and stop TestLogAppender
        rootLogger.removeAppender(testLogAppender);
        configuration.getAppenders().remove(testLogAppender.getName());
        testLogAppender.stop();
    }

    public TestLogAppender getTestLogAppender() {
        return testLogAppender;
    }
}
