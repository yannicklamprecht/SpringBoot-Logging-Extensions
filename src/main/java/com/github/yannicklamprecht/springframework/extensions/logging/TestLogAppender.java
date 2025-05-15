package com.github.yannicklamprecht.springframework.extensions.logging;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Plugin(name = "TestLogAppender", category = "Core", elementType = Appender.ELEMENT_TYPE)
public class TestLogAppender extends AbstractAppender {
    private final List<LogEvent> logMessages = Collections.synchronizedList(new ArrayList<>());

    public TestLogAppender(String name) {
        super(name, null, null, false, null);
    }

    @Override
    public void append(LogEvent event) {
        logMessages.add(event.toImmutable());
    }

    public List<LogEvent> getLogMessages() {
        return Collections.unmodifiableList(logMessages);
    }

    public void clear() {
        logMessages.clear();
    }
}
