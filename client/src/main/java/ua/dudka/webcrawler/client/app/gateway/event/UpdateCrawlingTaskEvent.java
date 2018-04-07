package ua.dudka.webcrawler.client.app.gateway.event;

import lombok.Data;

@Data
public class UpdateCrawlingTaskEvent {
    private final String taskId;
    private final String visitedLink;
}