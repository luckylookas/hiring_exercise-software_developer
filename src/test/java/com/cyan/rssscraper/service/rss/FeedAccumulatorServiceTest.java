package com.cyan.rssscraper.service.rss;

import com.cyan.rssscraper.Fixtures;
import com.cyan.rssscraper.client.RssClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedAccumulatorServiceTest {

    @Mock
    private RssClient rssClient;

    private FeedAccumulatorService feedAccumulatorService;

    @BeforeEach
    public void setUp() {
        feedAccumulatorService = new FeedAccumulatorService(rssClient);
    }

    @Test
    public void getFeeds_singleValidUri_returnsFeed() {
        when(rssClient.getFeed(eq(Fixtures.FEED_URL_1))).thenReturn(CompletableFuture.completedFuture(Fixtures.MOCKRSS1));
        assertThat(feedAccumulatorService.getFeeds(Set.of(Fixtures.FEED_URL_1))).hasSize(1);
    }
}
