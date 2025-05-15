package com.github.yannicklamprecht.springframework.extensions.logging;

import org.apache.logging.log4j.Level;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogAppenderTest {

    private static final Logger logger = LoggerFactory.getLogger(LogAppenderTest.class);

    @RegisterExtension
    private final Log4jTestExtension extension = new Log4jTestExtension();

    @Test
    void testLogCapture() {
        TestLogAppender logAppender = extension.getTestLogAppender();
        logger.info("This is a test log message");

        assertEquals(1, logAppender.getLogMessages().size());

        LogEvent first = logAppender.getLogMessages().get(0);
        assertEquals("This is a test log message",first.getMessage().getFormattedMessage());
        assertEquals(Level.INFO, first.getLevel());

        logger.warn("This is another test log message {}", "with a parameter");

        assertEquals(2, logAppender.getLogMessages().size());

        var second = logAppender.getLogMessages().get(1);
        assertEquals("This is another test log message with a parameter", second.getMessage().getFormattedMessage());
        assertEquals("with a parameter", second.getMessage().getParameters()[0]);
        assertEquals(Level.WARN, second.getLevel());


        Marker customMarker = MarkerManager.getMarker("CUSTOM_MARKER");
        var slfMarker = MarkerFactory.getIMarkerFactory().getMarker("CUSTOM_MARKER");

        logger.info(slfMarker, "This is a test log message");

        var third = logAppender.getLogMessages().get(2);
        assertEquals("This is a test log message", third.getMessage().getFormattedMessage());
        assertEquals(customMarker, third.getMarker());


    }
}
