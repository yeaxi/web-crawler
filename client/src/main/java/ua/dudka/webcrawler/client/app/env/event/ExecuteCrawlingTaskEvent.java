package ua.dudka.webcrawler.client.app.env.event;

import lombok.Value;

@Value
public class ExecuteCrawlingTaskEvent {
    private final String taskId;
    private final String link;
}