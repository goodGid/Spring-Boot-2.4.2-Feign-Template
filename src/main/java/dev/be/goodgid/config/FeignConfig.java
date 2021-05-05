package dev.be.goodgid.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.be.goodgid.feign.logger.FeignCustomLogger;
import feign.Logger;

@Configuration
public class FeignConfig {
    @Bean
    public Logger feignLogger(ApplicationContext applicationContext) {
        return new FeignCustomLogger(applicationContext);
    }
}
