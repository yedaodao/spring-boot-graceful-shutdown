package com.github.yedaodao.infrastructure.shutdown.configuration;

import com.github.yedaodao.infrastructure.shutdown.TomcatShutdown;
import com.github.yedaodao.infrastructure.shutdown.eureka.EurekaTomcatShutdownHandler;
import com.github.yedaodao.infrastructure.shutdown.properties.GracefulShutdownProperties;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GracefulShutdownProperties.class)
@ConditionalOnClass({Tomcat.class, DiscoveryClient.class})
@ConditionalOnProperty(
	value = {"spring.cloud.discovery.enabled"},
	matchIfMissing = true
)
@AutoConfigureAfter({TomcatGracefulShutdownConfiguration.class, EurekaClientAutoConfiguration.class})
public class EurekaGracefulShutdownConfiguration {
	@Autowired
	private TomcatShutdown shutdown;

	@Autowired
	private EurekaClient eurekaClient;

	@Bean
	public EurekaTomcatShutdownHandler tomcatShutdownHandler(GracefulShutdownProperties gracefulShutdownProperties) {
		return new EurekaTomcatShutdownHandler(shutdown, gracefulShutdownProperties, eurekaClient);
	}
}
