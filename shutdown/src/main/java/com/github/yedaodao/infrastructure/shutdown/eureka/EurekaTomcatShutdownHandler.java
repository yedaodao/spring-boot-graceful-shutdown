package com.github.yedaodao.infrastructure.shutdown.eureka;

import com.github.yedaodao.infrastructure.shutdown.Shutdown;
import com.github.yedaodao.infrastructure.shutdown.properties.GracefulShutdownProperties;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * Shutdown Handler for Eureka
 */
public class EurekaTomcatShutdownHandler implements ApplicationListener<ContextClosedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaTomcatShutdownHandler.class);

    private final Shutdown shutdown;
    private final GracefulShutdownProperties gracefulShutdownProperties;
    private final EurekaClient eurekaClient;

    public EurekaTomcatShutdownHandler(Shutdown shutdown, GracefulShutdownProperties gracefulShutdownProperties, EurekaClient eurekaClient) {
        this.shutdown = shutdown;
        this.gracefulShutdownProperties = gracefulShutdownProperties;
        this.eurekaClient = eurekaClient;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            LOGGER.info("Shutting down the eureka client");
            eurekaClient.shutdown();
            LOGGER.info("Waiting Eureka cluster confirm this instance shutdown during {} seconds ...", gracefulShutdownProperties.getWaitingSeconds().toString());
            Thread.sleep(gracefulShutdownProperties.getWaitingSeconds() * 1000);
            LOGGER.info("Starting pausing connector...");
            shutdown.pause();
            shutdown.shutdown(gracefulShutdownProperties.getTimeoutSeconds());
        } catch (InterruptedException ex) {
            LOGGER.error("Cannot shutdown gracefully: message={}", ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        }
    }
}
