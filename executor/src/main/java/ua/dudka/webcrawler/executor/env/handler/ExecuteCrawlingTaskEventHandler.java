package ua.dudka.webcrawler.executor.env.handler;

import ua.dudka.webcrawler.executor.domain.model.ExecuteCrawlingTaskEvent;

public interface ExecuteCrawlingTaskEventHandler {

    void handle(ExecuteCrawlingTaskEvent task);
}