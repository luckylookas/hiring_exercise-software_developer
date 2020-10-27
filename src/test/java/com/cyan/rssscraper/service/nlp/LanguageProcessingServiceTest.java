package com.cyan.rssscraper.service.nlp;

import com.cyan.rssscraper.NLPConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LanguageProcessingServiceTest {

    private final NLPConfig config = new NLPConfig("/nlpModels/en-pos-maxent.bin", "/nlpModels/en-lemmatizer.dict");
    private final LanguageProcessingService languageProcessingService = new LanguageProcessingService(config.getTokenizer(), config.getTagger(), config.getLemmatizer());

    private static Stream<Arguments> provideStringsForNlp() {
        return Stream.of(
                Arguments.of("5 workers injured in pipeline detonations in Jakarta's factory district", List.of("worker", "pipeline", "detonation", "jakarta", "factory", "district")),
                Arguments.of("Minister denies relationship with a pig", List.of("minister", "relationship", "pig")),
                Arguments.of("Latest bestselling romance novel turns out to be written by a pig", List.of("romance", "novel", "pig")),
                Arguments.of("Worrying obsession with pigs is infecting more and more cases.", List.of("obsession", "pig", "case"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringsForNlp")
    public void getImportantWords_simpleSentences_returnsLemmasForNouns(String sentence, List<String> tokens) {
        assertThat(languageProcessingService.getImportantWords(sentence))
                .containsExactlyInAnyOrderElementsOf(tokens);
    }
}
