package com.cyan.rssscraper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.lemmatizer.Lemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class NLPConfig {

    @Value("${app.nlp.tagger-model}")
    private String taggerModel;

    @Value("${app.nlp.lemma-dictionary}")
    private String lemmaDict;

    @Bean
    public Tokenizer getTokenizer() {
        return SimpleTokenizer.INSTANCE;
    }

    @Bean
    public POSTagger getTagger() {
        try (InputStream inputStreamPOSTagger = getClass().getResourceAsStream(taggerModel)) {
            POSModel posModel = new POSModel(inputStreamPOSTagger);
            return new POSTaggerME(posModel);
        } catch (IOException e) {
            //most likely the model files are not where they are supposed to be
            throw new BeanInitializationException("POSTagger", e);
        }
    }

    @Bean
    public Lemmatizer getLemmatizer() {
        try (InputStream dictLemmatizer = getClass().getResourceAsStream(lemmaDict)) {
            return new DictionaryLemmatizer(dictLemmatizer);
        } catch (IOException e) {
            //most likely the model files are not where they are supposed to be
            throw new BeanInitializationException("Lemmatizer", e);
        }
    }
}
