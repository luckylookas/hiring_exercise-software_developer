package com.cyan.rssscraper.service.analysis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AnalyzedTopic {
    @EqualsAndHashCode.Include
    private final String tag;

    @Builder.Default
    private final Set<String> feeds = new HashSet<>();
    @Builder.Default
    private final Set<AnalyzedItem> items = new HashSet<>();

    public int score() {
        return feeds.size();
    }
}
