package com.cyan.rssscraper.persistence.model;

import lombok.*;

import javax.persistence.*;

@Builder(toBuilder = true)
@Entity
@EqualsAndHashCode
@Table(name = "topic_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class TopicItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String title;
    private String link;

    @Column(name = "topic_id")
    private String topicId;
}
