package com.cyan.rssscraper.service.analysis;

import com.cyan.rssscraper.persistence.AnalysisPersistenceService;
import com.cyan.rssscraper.persistence.model.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisService {

    private final AnalysisWorkerService analysisWorkerService;
    private final AnalysisPersistenceService analysisPersistenceService;

    public String startAnalysis(Collection<URL> uris) {
        var id = analysisPersistenceService.create();
        analysisWorkerService.runAnalysis(uris).addCallback(
                result -> analysisPersistenceService.updateTopics(id, result),
                error -> analysisPersistenceService.setError(id, error.getMessage())
        );
        return id;
    }

    public Optional<Boolean> isReady(String id) {
        return analysisPersistenceService.isReady(id);
    }

    public Optional<Set<Topic>> getTopics(String id) {
        return analysisPersistenceService.findTopics(id);
    }

    public Optional<String> getError(String id) {
        return analysisPersistenceService.findError(id);
    }
}
