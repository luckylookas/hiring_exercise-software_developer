package com.cyan.rssscraper.controller.model;

import com.cyan.rssscraper.persistence.model.Topic;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TopicMapper {

    public TopicDto toDto(Topic topic) {
        return TopicDto.builder()
                .tag(topic.getTag())
                .items(topic.getItems().stream()
                        .map(it -> TopicItemDto.builder()
                                .link(it.getLink())
                                .title(it.getTitle())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
