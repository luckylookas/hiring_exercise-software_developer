package com.cyan.rssscraper.persistence.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder(toBuilder = true)
@Entity
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Analysis {

    @Id
    private String id;

    private boolean ready;
    private String error;


    //eager as I always need them anyway
    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "analysis_id")
    @Singular
    private Set<Topic> topics;
}
