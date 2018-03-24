package ua.dudka.webcrawler.executor.domain.service;

import java.util.Set;

public interface WebPageParser {
    Set<String> parse(String link);
}
