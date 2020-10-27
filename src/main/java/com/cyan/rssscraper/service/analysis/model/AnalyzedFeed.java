package com.cyan.rssscraper.service.analysis.model;

import lombok.*;

import java.util.Set;

@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AnalyzedFeed {

    private String title;
    private String link;

    @Singular
    @EqualsAndHashCode.Exclude
    private Set<AnalyzedItem> items;
}
