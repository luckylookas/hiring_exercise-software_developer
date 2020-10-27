package com.cyan.rssscraper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app")
@Getter
@Setter
@NoArgsConstructor
public class AppConfig {

    private Integer topicsCount;
    private Integer timeoutMs;
    private Integer hotnessThreshold;
}
