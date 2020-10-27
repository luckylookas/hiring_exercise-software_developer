package com.cyan.rssscraper.persistence.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder(toBuilder = true)
@Entity
@EqualsAndHashCode
@Table(name = "topic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String tag;

    //eager as I always need them anyway
    @Singular
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "topic_id")
    @EqualsAndHashCode.Exclude
    private Set<TopicItem> items;
}
