package com.github.yedaodao.infrastructure.shutdown.configuration;

import com.github.yedaodao.infrastructure.shutdown.TomcatShutdown;
import com.github.yedaodao.infrastructure.shutdown.properties.GracefulShutdownProperties;
import com.github.yedaodao.infrastructure.shutdown.tomcat.TomcatShutdownHandler;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
    public EmbeddedServletContainerCustomizer tomcatCustomizer(TomcatShutdown tomcatShutdown) {
        return container -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                ((TomcatEmbeddedServletContainerFactory) container)
                        .addConnectorCustomizers(tomcatShutdown);
            }
        };
    }
}
