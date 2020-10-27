package com.cyan.rssscraper.service.nlp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.lemmatizer.Lemmatizer;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class LanguageProcessingService {

    public static final String NO_LEMMA_AVAILABLE_FLAG = "o";
    //Proper Noun plural/singular, Noun plural/singular
    private static final Set<String> significantTags = Set.of("NNPS", "NNP", "NNS", "NN");

    private final Tokenizer tokenizer;
    private final POSTagger tagger;
    private final Lemmatizer lemmatizer;

    @SneakyThrows
    public Set<String> getImportantWords(String input) {
        String[] tokens = tokenizer.tokenize(input.toLowerCase());
        String[] tags = tagger.tag(tokens);
        String[] lemmas = lemmatizer.lemmatize(tokens, tags);

        return IntStream.range(0, tokens.length).boxed()
                .filter(index -> significantTags.contains(tags[index]))
                .map(index -> NO_LEMMA_AVAILABLE_FLAG.equals(lemmas[index].toLowerCase()) ? tokens[index] : lemmas[index])
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
