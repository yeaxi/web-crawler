package ua.dudka.webcrawler.executor.domain.event;

import lombok.Data;

@Data
public class UpdateCrawlingTaskEvent {
    private final String taskId;
    private final String foundLink;

}