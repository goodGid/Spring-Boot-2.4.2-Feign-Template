package dev.be.goodgid.feign.config;

import org.springframework.context.annotation.Bean;

import dev.be.goodgid.feign.decoder.DemoFeignErrorDecoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DemoFeignConfig {

    @Bean
    public DemoFeignErrorDecoder DemoErrorDecoder() {
        return new DemoFeignErrorDecoder();
    }

}
