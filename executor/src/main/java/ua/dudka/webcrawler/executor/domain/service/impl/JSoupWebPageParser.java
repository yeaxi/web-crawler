package ua.dudka.webcrawler.executor.domain.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ua.dudka.webcrawler.executor.domain.service.WebPageParser;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JSoupWebPageParser implements WebPageParser {

    @Override
    public Set<String> parse(String link) {
        log.info("Parsing link {}", link);
        try {
            return parseLink(link);
        } catch (Exception e) {
            log.warn("Error during parsing link {} - {} ", link, e.getMessage());
        }
        return Collections.emptySet();
    }

    private Set<String> parseLink(String link) throws IOException {
        Document document = Jsoup.connect(link).get();
        Elements linksOnPage = document.select("a[href]");
        return linksOnPage.stream()
                .map(e -> e.attr("abs:href"))
                .collect(Collectors.toSet());
    }
}