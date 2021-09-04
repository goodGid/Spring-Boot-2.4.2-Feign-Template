package dev.be.goodgid.feign.config;

import org.springframework.context.annotation.Bean;

import dev.be.goodgid.feign.decoder.DemoFeignErrorDecoder;
import dev.be.goodgid.feign.interceptor.DemoFeignInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
public class DemoFeignConfig {

    @Bean
    public DemoFeignErrorDecoder DemoErrorDecoder() {
        return new DemoFeignErrorDecoder();
    }

    @Bean
    public DemoFeignInterceptor multiLanguageRequestInterceptor() {
        return DemoFeignInterceptor.of();
    }
}
