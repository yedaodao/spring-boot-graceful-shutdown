package com.github.yedaodao.infrastructure.shutdown.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "shutdown")
public class GracefulShutdownProperties {
	/**
	 * Wait seconds for ensuring the instance is marked offline
	 *
	 * The default value is calculated by following formula:
	 * 2*response-cache-update-interval-ms(3s)+2*registry-fetch-interval-seconds(1s)+2*ServerListRefreshInterval(2s)+1s(Correction)
	 */
	private Integer waitingSeconds = 13;

	/**
	 * Wait seconds for tasks in thread pool
	 */
	private Integer timeoutSeconds = 15;

	public Integer getWaitingSeconds() {
		return waitingSeconds;
	}

	public void setWaitingSeconds(Integer waitingSeconds) {
		this.waitingSeconds = waitingSeconds;
	}

	public Integer getTimeoutSeconds() {
		return timeoutSeconds;
	}

	public void setTimeoutSeconds(Integer timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}
}
