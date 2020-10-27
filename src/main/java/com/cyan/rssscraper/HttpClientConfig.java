package com.cyan.rssscraper;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {

    private final AppConfig appConfig;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(appConfig.getTimeoutMs()))
                .setReadTimeout(Duration.ofMillis(appConfig.getTimeoutMs()))
                .build();
    }

}
