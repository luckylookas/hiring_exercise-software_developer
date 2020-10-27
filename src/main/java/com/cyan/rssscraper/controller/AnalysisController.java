package com.cyan.rssscraper.controller;

import com.cyan.rssscraper.controller.model.TopicDto;
import com.cyan.rssscraper.controller.model.TopicMapper;
import com.cyan.rssscraper.service.analysis.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService rssService;
    private final TopicMapper topicMapper;

    @PostMapping(
            path = "/new",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "analyze feeds for hot topics",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Set.class))))
    @ApiResponses({@ApiResponse(responseCode = "201",
            description = "start an analysis",
            content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "400", description = "one of the supplied uri where malformed"),
            @ApiResponse(responseCode = "422", description = "an analysis needs at least two feeds to work")})
    public ResponseEntity<String> analyze(@RequestBody Set<URL> uris) {
        if (uris.size() < 2) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(rssService.startAnalysis(uris));
    }

    @Operation(summary = "get results for an analysis")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "get the 3 most common topics form the analysis",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Set.class))}),
            @ApiResponse(responseCode = "404", description = "unknown id"),
            @ApiResponse(responseCode = "425", description = "analysis has not yet finished"),
            @ApiResponse(responseCode = "500", description = "analysis failed", content = {@Content(mediaType = "text/plain")})
    })
    @GetMapping(value = "/frequency/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<TopicDto>> getFrequency(@PathVariable("id") String analysisId) {
        if (!rssService.isReady(analysisId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND))) {
            return ResponseEntity.status(HttpStatus.TOO_EARLY).build();
        }

        return rssService.getTopics(analysisId)
                .map(it -> it.stream()
                        .map(topicMapper::toDto)
                        .collect(Collectors.toSet()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalStateException(rssService.getError(analysisId).orElse("unknown error")));
    }
}
