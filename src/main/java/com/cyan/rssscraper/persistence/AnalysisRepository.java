package com.cyan.rssscraper.persistence;

import com.cyan.rssscraper.persistence.model.Analysis;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalysisRepository extends CrudRepository<Analysis, String> {

    @Query("SELECT a.ready FROM Analysis a WHERE a.id = ?1")
    Optional<Boolean> isReady(String id);

    Optional<Analysis> findByIdAndReadyIsTrue(String id);

    @Query("SELECT a.error FROM Analysis a WHERE a.id = ?1 AND a.ready = true")
    Optional<String> findErrorByIdAndReadyIsTrue(String id);
}
