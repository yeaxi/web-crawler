package ua.dudka.webcrawler.executor.domain.model.vo;

import lombok.Value;

@Value(staticConstructor = "from")
public class StartPage {
    String url;
    String alias;
}