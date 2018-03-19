package ua.dudka.webcrawler.client.domain.model;

import lombok.Value;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Value
public class StartPage {

    private final String link;
    private final int maxVisitedLinks;
    private final Set<String> visitedLinks = ConcurrentHashMap.newKeySet();

    StartPage() {
        this.link = "";
        this.maxVisitedLinks = 0;
    }

    private StartPage(String link, int maxVisitedLinks) {
        this.link = link;
        this.maxVisitedLinks = maxVisitedLinks;
    }

    public static StartPage of(String link) {
        return new StartPage(link, 1);
    }

    public static StartPage of(String link, int maxVisitedLinks) {
        return new StartPage(link, maxVisitedLinks);
    }

}