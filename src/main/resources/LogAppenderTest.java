package com.github.yannicklamprecht.springframework.extensions.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogAppenderTest {

    private static final Logger logger = LogManager.getLogger(LogAppenderTest.class);

    @RegisterExtension
    private Log4jTestExtension extension;

    @Test
    void testLogCapture() {
        logger.info("This is a test log message");
        TestLogAppender logAppender = extension.getTestLogAppender();

        assertTrue(logAppender.getLogMessages().contains("This is a test log message"));
    }
}
