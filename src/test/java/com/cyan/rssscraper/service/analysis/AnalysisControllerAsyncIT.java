package com.cyan.rssscraper.service.analysis;

import com.cyan.rssscraper.client.RssClient;
import com.cyan.rssscraper.controller.AnalysisController;
import com.cyan.rssscraper.controller.model.TopicDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.cyan.rssscraper.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Disabled("run to check if Async is configured correctly")
public class AnalysisControllerAsyncIT {

    @Autowired
    private AnalysisController analysisController;

    @MockBean
    private RssClient rssClient;

    @BeforeEach
    public void setUp() {
        when(rssClient.getFeed(eq(FEED_URL_1))).thenReturn(CompletableFuture.supplyAsync(() -> MOCKRSS1, CompletableFuture.delayedExecutor(4000, TimeUnit.MILLISECONDS)));
        when(rssClient.getFeed(eq(FEED_URL_2))).thenReturn(CompletableFuture.supplyAsync(() -> MOCKRSS2, CompletableFuture.delayedExecutor(4000, TimeUnit.MILLISECONDS)));
    }

    @Test
    public void async_twoSlowFeeds_fetchedParalelly() throws InterruptedException {
        var id = analysisController.analyze(Set.of(FEED_URL_1, FEED_URL_2));
        assertThat(analysisController.getFrequency(id.getBody()).getStatusCode()).isEqualTo(HttpStatus.TOO_EARLY);

        Thread.sleep(5000);
        assertThat(analysisController.getFrequency(id.getBody()).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(analysisController.getFrequency(id.getBody()).getBody())
                .extracting(TopicDto::getTag)
                .containsOnly("worker", "jakarta");
    }
}
