package com.cyan.rssscraper.controller.model;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class TopicItemDto {

    private String title;
    private String link;

}
