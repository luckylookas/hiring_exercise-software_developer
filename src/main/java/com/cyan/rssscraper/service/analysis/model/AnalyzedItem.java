package com.cyan.rssscraper.service.analysis.model;

import lombok.*;

import java.util.Set;

@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AnalyzedItem {

    @EqualsAndHashCode.Include
    private String title;
    private String link;
    @Singular
    private Set<String> tokens;
}
