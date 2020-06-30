package com.github.yedaodao.infrastructure.shutdown;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Shutdown component for Tomcat
 */
public class TomcatShutdown implements Shutdown, TomcatConnectorCustomizer {
    private volatile Connector connector;

    private static final Logger LOGGER = LoggerFactory.getLogger(TomcatShutdown.class);

    @Override
    public void pause() {
        if (connector != null) {
            connector.pause();
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void shutdown(Integer delay) throws InterruptedException {
        final Executor executor = connector.getProtocolHandler().getExecutor();
        if (!(executor instanceof ThreadPoolExecutor)) {
            return;
        }
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
        threadPoolExecutor.shutdown();
        if (!threadPoolExecutor.awaitTermination(delay, TimeUnit.SECONDS)) {
            LOGGER.warn("Tomcat thread pool did not shut down gracefully within {} seconds. Proceeding with forceful shutdown", delay);

            threadPoolExecutor.shutdownNow();
        }
    }

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }
}
