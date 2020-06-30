package com.github.yedaodao.infrastructure.shutdown;

public interface Shutdown {
    /**
     * Pause current web instance
     *
     * @throws InterruptedException Cannot shutdown gracefully
     */
    void pause() throws InterruptedException;

    /**
     * Shutdown the web thread pool
     *
     * @param delaySeconds seconds for waiting before shutdown
     * @throws InterruptedException Cannot shutdown gracefully
     */
    void shutdown(Integer delaySeconds) throws InterruptedException;
}
