package ua.dudka.webcrawler.executor.env.handler;

import ua.dudka.webcrawler.executor.domain.model.ExecuteCrawlingTaskEvent;

public interface CrawlingTaskExecutionHandler {

    void handle(ExecuteCrawlingTaskEvent task);
}