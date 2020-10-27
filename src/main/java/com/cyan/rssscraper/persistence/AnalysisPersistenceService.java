package com.cyan.rssscraper.persistence;

import com.cyan.rssscraper.persistence.model.Analysis;
import com.cyan.rssscraper.persistence.model.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnalysisPersistenceService {
    private final AnalysisRepository analysisRepository;

    public Optional<Set<Topic>> findTopics(String analysisId) {
        return analysisRepository.findByIdAndReadyIsTrue(analysisId)
                .map(Analysis::getTopics).filter(it -> !it.isEmpty());
    }

    public String create() {
        var uuid = UUID.randomUUID();
        analysisRepository.save(Analysis.builder().id(uuid.toString()).ready(false).build());
        return uuid.toString();
    }

    public void updateTopics(String id, List<Topic> topics) {
        var analysis = analysisRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        analysisRepository.save(analysis.toBuilder().ready(true).topics(topics).build());

    }

    public void setError(String id, String error) {
        var analysis = analysisRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        analysisRepository.save(analysis.toBuilder().ready(true).error(error).build());
    }

    public Optional<Boolean> isReady(String id) {
        return analysisRepository.isReady(id);
    }

    public Optional<String> findError(String id) {
        return analysisRepository.findErrorByIdAndReadyIsTrue(id);
    }
}
