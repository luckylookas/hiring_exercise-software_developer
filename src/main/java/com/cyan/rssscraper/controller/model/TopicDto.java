package com.cyan.rssscraper.controller.model;

import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class TopicDto {

    private String tag;

    @EqualsAndHashCode.Exclude
    private Set<TopicItemDto> items;
}
