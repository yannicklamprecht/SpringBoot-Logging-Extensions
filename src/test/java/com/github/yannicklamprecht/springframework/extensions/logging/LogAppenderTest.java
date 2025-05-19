package com.github.yannicklamprecht.springframework.extensions.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.test.appender.ListAppender;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({Log4jTestExtension.class})
public class LogAppenderTest {

    private static final Logger logger = LoggerFactory.getLogger(LogAppenderTest.class);

    private ListAppender logAppender;

    @Test
    void testLogCapture() {

        logger.info("This is a test log message");

        assertThat(logAppender)
                .extracting(ListAppender::getEvents)
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .asInstanceOf(InstanceOfAssertFactories.type(LogEvent.class))
                .returns("This is a test log message", event -> event.getMessage().getFormattedMessage())
                .returns(Level.INFO, LogEvent::getLevel);

        logger.warn("This is another test log message {}", "with a parameter");


        assertThat(logAppender)
                .extracting(ListAppender::getEvents)
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .hasSize(2)
                .last()
                .asInstanceOf(InstanceOfAssertFactories.type(LogEvent.class))
                .returns("This is another test log message with a parameter", e -> e.getMessage().getFormattedMessage())
                .returns("with a parameter", e -> e.getMessage().getParameters()[0])
                .returns(Level.WARN, LogEvent::getLevel);


        Marker customMarker = MarkerManager.getMarker("CUSTOM_MARKER");
        var slfMarker = MarkerFactory.getIMarkerFactory().getMarker("CUSTOM_MARKER");

        logger.info(slfMarker, "This is a test log message");


        assertThat(logAppender)
                .extracting(ListAppender::getEvents)
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .hasSize(3)
                .last()
                .asInstanceOf(InstanceOfAssertFactories.type(LogEvent.class))
                .returns("This is a test log message", event -> event.getMessage().getFormattedMessage())
                .returns(customMarker, LogEvent::getMarker);
    }

    @Test
    void testLogCaptureV2() {
        logger.info("This is a test log message");


        assertThat(logAppender)
                .extracting(ListAppender::getEvents)
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .hasSize(1)
                .first()
                .asInstanceOf(InstanceOfAssertFactories.type(LogEvent.class))
                .returns("This is a test log message", event -> event.getMessage().getFormattedMessage())
                .returns(Level.INFO, LogEvent::getLevel);

        logger.warn("This is another test log message {}", "with a parameter");

        assertThat(logAppender)
                .extracting(ListAppender::getEvents)
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .hasSize(2)
                .last()
                .asInstanceOf(InstanceOfAssertFactories.type(LogEvent.class))
                .returns("This is another test log message with a parameter", e -> e.getMessage().getFormattedMessage())
                .returns("with a parameter", e -> e.getMessage().getParameters()[0])
                .returns(Level.WARN, LogEvent::getLevel);


        Marker customMarker = MarkerManager.getMarker("CUSTOM_MARKER");
        var slfMarker = MarkerFactory.getIMarkerFactory().getMarker("CUSTOM_MARKER");

        logger.info(slfMarker, "This is a test log message");

        assertThat(logAppender)
                .extracting(ListAppender::getEvents)
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .hasSize(3)
                .last()
                .asInstanceOf(InstanceOfAssertFactories.type(LogEvent.class))
                .returns("This is a test log message", event -> event.getMessage().getFormattedMessage())
                .returns(customMarker, LogEvent::getMarker);

    }
}
