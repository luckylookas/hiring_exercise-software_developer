package com.cyan.rssscraper.service.rss.model;

import com.cyan.rssscraper.client.model.Rss;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class FeedResponse {

    public static final FeedResponse UNKNOWN_ERROR = FeedResponse.builder().error(new IllegalStateException("unknown error")).build();

    private final Rss feed;
    private final Throwable error;

    public boolean hasError() {
        return error != null;
    }

    public boolean noError() {
        return !hasError();
    }
}
