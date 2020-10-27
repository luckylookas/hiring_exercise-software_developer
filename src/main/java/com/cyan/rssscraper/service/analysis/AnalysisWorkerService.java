package com.cyan.rssscraper.service.analysis;

import com.cyan.rssscraper.AppConfig;
import com.cyan.rssscraper.client.model.Item;
import com.cyan.rssscraper.persistence.model.Topic;
import com.cyan.rssscraper.service.analysis.model.AnalyzedFeed;
import com.cyan.rssscraper.service.analysis.model.AnalyzedItem;
import com.cyan.rssscraper.service.nlp.LanguageProcessingService;
import com.cyan.rssscraper.service.rss.FeedAccumulatorService;
import com.cyan.rssscraper.service.rss.model.FeedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisWorkerService {

    private final LanguageProcessingService languageProcessingService;
    private final FeedAccumulatorService feedAccumulatorService;
    private final TopicFinder topicFinder;
    private final AppConfig appConfig;

    @Async
    public ListenableFuture<List<Topic>> runAnalysis(Collection<URL> uris) {
        var feeds = feedAccumulatorService.getFeeds(uris);

        Optional<Throwable> err;
        if ((err = getBreakingError(feeds)).isPresent()) {
            return AsyncResult.forExecutionException(err.get());
        }

        var analyzedFeeds = feeds.stream()
                .filter(FeedResponse::noError)
                .map(FeedResponse::getFeed)
                .map(feed -> AnalyzedFeed.builder()
                        .title(feed.getChannel().getTitle())
                        .link(feed.getChannel().getLink())
                        .items(feed.getChannel().getItems().stream().map(this::analyzeItem).collect(Collectors.toSet()))
                        .build()
                ).collect(Collectors.toSet());

        return AsyncResult.forValue(topicFinder.findTopics(analyzedFeeds, appConfig.getTopicsCount()));
    }

    private AnalyzedItem analyzeItem(Item item) {
        return AnalyzedItem.builder()
                .title(item.getTitle())
                .link(item.getLink())
                .tokens(languageProcessingService.getImportantWords(item.getTitle()))
                .build();
    }

    private Optional<Throwable> getBreakingError(Set<FeedResponse> feeds) {
        if (feeds.stream().anyMatch(FeedResponse::hasError)) {
            var ratio = feeds.stream().filter(FeedResponse::hasError).count() + "/" + feeds.size();
            var collectedErrors = feeds.stream().filter(FeedResponse::hasError).map(it -> it.getError().getMessage()).collect(Collectors.joining(","));
            log.debug("{} urls responded with exceptions: {}", ratio, collectedErrors);
            if (feeds.stream().filter(FeedResponse::noError).count() < appConfig.getHotnessThreshold()) {
                return Optional.of(new IllegalArgumentException("analysis requires >2 feeds, " + ratio + " urls failed, errors: [" + collectedErrors + "]"));
            }
        }
        return Optional.empty();
    }
}
