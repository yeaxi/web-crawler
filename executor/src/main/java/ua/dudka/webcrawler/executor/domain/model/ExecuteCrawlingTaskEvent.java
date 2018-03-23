package ua.dudka.webcrawler.executor.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class ExecuteCrawlingTaskEvent {

    private final String id;
    private final String link;

    ExecuteCrawlingTaskEvent() {
        this.id = "";
        this.link = "";
    }
}