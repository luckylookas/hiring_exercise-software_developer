package com.cyan.rssscraper.client;

import com.cyan.rssscraper.client.model.Rss;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RssClient {

    private final RestTemplate restTemplate;

    @Async
    public CompletableFuture<Rss> getFeed(@NonNull URL url) {
        return CompletableFuture.completedFuture(httpGet(url));
    }

    @SneakyThrows
    private Rss httpGet(URL url) {
        return restTemplate.getForObject(url.toURI(), Rss.class);
    }
}
