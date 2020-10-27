package com.cyan.rssscraper.service.rss;

import com.cyan.rssscraper.client.RssClient;
import com.cyan.rssscraper.service.rss.model.FeedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.cyan.rssscraper.service.rss.model.FeedResponse.UNKNOWN_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedAccumulatorService {

    private final RssClient rssClient;

    public Set<FeedResponse> getFeeds(Collection<URL> uris) {
        Set<CompletableFuture<FeedResponse>> feeds = uris.stream()
                .map(rssClient::getFeed)
                .map(it -> it
                        .thenApply(response -> FeedResponse.builder().feed(response).build())
                        .exceptionally(err -> FeedResponse.builder().error(err).build())
                )
                .collect(Collectors.toSet());

        // cannot work with the native completion exceptions, as they get rethrown every time and prevent accessing them
        // wrapping errors in 'valid' responses is the simplest way to keep this information around
        // also means, there can never be a CompletionException (CompletableFuture::exceptionally)

        CompletableFuture.allOf(feeds.toArray(new CompletableFuture[0])).join();
        return feeds.stream().map(it -> it.getNow(UNKNOWN_ERROR)).collect(Collectors.toSet());
    }
}
