package com.github.yedaodao.infrastructure.shutdown.configuration;

import com.github.yedaodao.infrastructure.shutdown.TomcatShutdown;
import com.github.yedaodao.infrastructure.shutdown.properties.GracefulShutdownProperties;
import com.github.yedaodao.infrastructure.shutdown.tomcat.TomcatShutdownHandler;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GracefulShutdownProperties.class)
@ConditionalOnClass({Tomcat.class})
public class TomcatGracefulShutdownConfiguration {
    @Bean
    public TomcatShutdown shutdown() {
        return new TomcatShutdown();
    }

    @Bean
    @ConditionalOnMissingClass({"com.netflix.discovery.DiscoveryClient"})
    public TomcatShutdownHandler tomcatShutdownHandler(TomcatShutdown tomcatShutdown, GracefulShutdownProperties gracefulShutdownProperties) {
        return new TomcatShutdownHandler(tomcatShutdown, gracefulShutdownProperties);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatServletCustomizer(TomcatShutdown tomcatShutdown) {
        return factory -> factory.addConnectorCustomizers(tomcatShutdown);
    }
}
