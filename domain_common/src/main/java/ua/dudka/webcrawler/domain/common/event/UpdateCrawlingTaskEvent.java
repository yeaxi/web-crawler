package ua.dudka.webcrawler.domain.common.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateCrawlingTaskEvent {
    private final String taskId;
    private final String visitedLink;

    UpdateCrawlingTaskEvent() {
        this.taskId = "";
        this.visitedLink = "";
    }
}