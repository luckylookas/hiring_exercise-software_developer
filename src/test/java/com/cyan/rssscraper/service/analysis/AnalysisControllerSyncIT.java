package com.cyan.rssscraper.service.analysis;

import com.cyan.rssscraper.client.RssClient;
import com.cyan.rssscraper.controller.model.TopicDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.cyan.rssscraper.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("sync")
public class AnalysisControllerSyncIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private RssClient rssClient;

    @Test
    public void startAndGet_straightCase_returnsValidResponse() {
        when(rssClient.getFeed(eq(FEED_URL_1))).thenReturn(CompletableFuture.completedFuture(MOCKRSS1));
        when(rssClient.getFeed(eq(FEED_URL_2))).thenReturn(CompletableFuture.completedFuture(MOCKRSS2));

        var id = testRestTemplate.postForEntity("http://localhost:" + port + "/new", Set.of(FEED_URL_1, FEED_URL_2), String.class);
        var response = testRestTemplate.getForEntity("http://localhost:" + port + "/frequency/" + id.getBody(), TopicDto[].class);

        assertThat(response.getBody())
                .extracting(TopicDto::getTag)
                .containsOnly("worker", "jakarta");
    }

    @Test
    public void startAndGet_breakingError_returnsErrorResponse() {
        when(rssClient.getFeed(FEED_URL_1)).thenReturn(CompletableFuture.failedFuture(new IllegalStateException("mock1")));
        when(rssClient.getFeed(eq(FEED_URL_2))).thenReturn(CompletableFuture.completedFuture(MOCKRSS2));
        when(rssClient.getFeed(eq(FEED_URL_100))).thenReturn(CompletableFuture.failedFuture(new IllegalStateException("mock100")));

        var id = testRestTemplate.postForEntity("http://localhost:" + port + "/new", Set.of(FEED_URL_1, FEED_URL_2, FEED_URL_100), String.class);
        var response = testRestTemplate.getForEntity("http://localhost:" + port + "/frequency/" + id.getBody(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void startAndGet_recoverableError_returnsValidResponse() {
        when(rssClient.getFeed(eq(FEED_URL_1))).thenReturn(CompletableFuture.completedFuture(MOCKRSS1));
        when(rssClient.getFeed(eq(FEED_URL_2))).thenReturn(CompletableFuture.completedFuture(MOCKRSS2));
        when(rssClient.getFeed(eq(FEED_URL_100))).thenReturn(CompletableFuture.failedFuture(new IllegalStateException("mock100")));

        var id = testRestTemplate.postForEntity("http://localhost:" + port + "/new", Set.of(FEED_URL_1, FEED_URL_2, FEED_URL_100), String.class);
        var response = testRestTemplate.getForEntity("http://localhost:" + port + "/frequency/" + id.getBody(), TopicDto[].class);

        assertThat(response.getBody())
                .extracting(TopicDto::getTag)
                .containsOnly("worker", "jakarta");
    }
}
