package ua.dudka.webcrawler.executor.domain.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class StartPage {

    private final String link;
}