package com.cyan.rssscraper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

@Component
@Slf4j
public class URIConverter {
    public Optional<URI> toUriIfValid(String string) {
        try {
            return Optional.of(URI.create(string));
        } catch (IllegalArgumentException ex) {
            log.trace("invalid uri: {}", string);
            return Optional.empty();
        }
    }
}
