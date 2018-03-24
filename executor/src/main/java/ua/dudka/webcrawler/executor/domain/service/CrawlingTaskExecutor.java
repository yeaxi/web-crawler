package ua.dudka.webcrawler.executor.domain.service;

import ua.dudka.webcrawler.executor.domain.model.ExecuteCrawlingTaskEvent;

public interface CrawlingTaskExecutor {
    void execute(ExecuteCrawlingTaskEvent event);
}
