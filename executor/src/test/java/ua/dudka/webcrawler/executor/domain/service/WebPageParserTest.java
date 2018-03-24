package ua.dudka.webcrawler.executor.domain.service;

import org.junit.jupiter.api.Test;
import ua.dudka.webcrawler.executor.domain.service.impl.JSoupWebPageParser;

import static org.assertj.core.api.Assertions.assertThat;

class WebPageParserTest {

    @Test
    void parseValidLinkShouldNotReturnEmptyCollections() {
        WebPageParser pageParser = new JSoupWebPageParser();

        assertThat(pageParser.parse("https://google.com")).isNotEmpty();
    }

    @Test
    void parseNotValidLinkShouldReturnEmptyCollections() {
        WebPageParser pageParser = new JSoupWebPageParser();

        assertThat(pageParser.parse("ttps://google.com")).isEmpty();
    }
}