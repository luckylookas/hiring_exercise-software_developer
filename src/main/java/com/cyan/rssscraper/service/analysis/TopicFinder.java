package com.cyan.rssscraper.service.analysis;

import com.cyan.rssscraper.AppConfig;
import com.cyan.rssscraper.persistence.model.Topic;
import com.cyan.rssscraper.persistence.model.TopicItem;
import com.cyan.rssscraper.service.analysis.model.AnalyzedFeed;
import com.cyan.rssscraper.service.analysis.model.AnalyzedTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicFinder {

    private final AppConfig appConfig;

    public List<Topic> findTopics(Set<AnalyzedFeed> analyzedFeeds, int count) {
        Map<String, AnalyzedTopic> topics = new HashMap<>();

        // todo is there a solution for streaming ?

        analyzedFeeds.forEach(feed -> {
            feed.getItems().forEach(item -> {
                item.getTokens().forEach(tag -> {
                    topics.putIfAbsent(tag, AnalyzedTopic.builder().tag(tag).build());
                    topics.get(tag).getFeeds().add(feed.getLink());
                    topics.get(tag).getItems().add(item);
                });
            });
        });

        return topics.values().stream()
                .filter(topic -> topic.score() >= appConfig.getHotnessThreshold())
                .sorted((topic1, topic2) -> topic2.score() - topic1.score())
                .limit(count)
                .map(hotTopic -> Topic.builder()
                        .tag(hotTopic.getTag())
                        .items(hotTopic.getItems()
                                .stream()
                                .map(item -> TopicItem.builder()
                                        .title(item.getTitle())
                                        .link(item.getLink())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
