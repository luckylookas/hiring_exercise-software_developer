package com.cyan.rssscraper.service.rss;

import com.cyan.rssscraper.client.model.Item;
import com.cyan.rssscraper.client.model.Rss;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class RssXmlMappingTest {

    private final XmlMapper xmlMapper = new XmlMapper();

    @Test
    public void map_validFeed_unwrapsCDataAndPlainStrings() throws IOException {
        try (
                var xml = this.getClass().getResourceAsStream("/testdata/rss/test.rss.xml");
        ) {
            String str = IOUtils.toString(xml, StandardCharsets.UTF_8.name());
            Rss value = xmlMapper.readValue(str, Rss.class);
            assertThat(value.getChannel().getItems()).hasSize(2);
            assertThat(value.getChannel().getItems()).extracting(Item::getTitle).containsOnly("test1", "test2");
        }


    }
}
