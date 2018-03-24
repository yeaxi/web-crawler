package ua.dudka.webcrawler.executor.env.publisher;

import ua.dudka.webcrawler.executor.domain.model.UpdateCrawlingTaskEvent;

public interface UpdateCrawlingTaskEventPublisher {
    void publishEvent(UpdateCrawlingTaskEvent event);
}
