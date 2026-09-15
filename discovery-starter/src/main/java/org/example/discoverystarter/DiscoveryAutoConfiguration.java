package org.example.discoverystarter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(DiscoveryProperties.class)
public class DiscoveryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DiscoveryClient.class)
    public DiscoveryClient discoveryClient(DiscoveryProperties props) {
        return new DiscoveryClient(props.getServerUrl());
    }

    @Bean
    @ConditionalOnMissingBean(RegistrationService.class)
    public RegistrationService registrationService(DiscoveryProperties props) {
        return new RegistrationService(props);
    }
}