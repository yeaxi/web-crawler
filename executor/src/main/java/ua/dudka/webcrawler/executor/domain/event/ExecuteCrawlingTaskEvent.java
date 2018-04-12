package ua.dudka.webcrawler.executor.domain.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class ExecuteCrawlingTaskEvent {

    private final String taskId;
    private final String link;

    ExecuteCrawlingTaskEvent() {
        this.taskId = "";
        this.link = "";
    }
}