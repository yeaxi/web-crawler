package ua.dudka.webcrawler.executor.domain.service;

import ua.dudka.webcrawler.executor.domain.event.ExecuteCrawlingTaskEvent;

public interface CrawlingTaskExecutor {
    void execute(ExecuteCrawlingTaskEvent event);
}
