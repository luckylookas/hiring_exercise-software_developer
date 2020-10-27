package com.cyan.rssscraper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@Profile("!sync")
@Slf4j
public class AsyncConfig {

    public AsyncConfig() {
        log.info("ASYNC EXECUTIONS ARE ENABLED.");
    }
}
