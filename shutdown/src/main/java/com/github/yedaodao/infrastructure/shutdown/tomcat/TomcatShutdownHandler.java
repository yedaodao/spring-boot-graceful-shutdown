package com.github.yedaodao.infrastructure.shutdown.tomcat;

import com.github.yedaodao.infrastructure.shutdown.Shutdown;
import com.github.yedaodao.infrastructure.shutdown.properties.GracefulShutdownProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * Shutdown Handler for Tomcat
 */
public class TomcatShutdownHandler implements ApplicationListener<ContextClosedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TomcatShutdownHandler.class);

    private final Shutdown shutdown;
    private final GracefulShutdownProperties gracefulShutdownProperties;

    public TomcatShutdownHandler(Shutdown shutdown, GracefulShutdownProperties gracefulShutdownProperties) {
        this.shutdown = shutdown;
        this.gracefulShutdownProperties = gracefulShutdownProperties;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            LOGGER.info("Starting pausing connector...");
            shutdown.pause();
            shutdown.shutdown(gracefulShutdownProperties.getTimeoutSeconds());
        } catch (InterruptedException ex) {
            LOGGER.error("Cannot shutdown gracefully: message={}", ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        }
    }
}
