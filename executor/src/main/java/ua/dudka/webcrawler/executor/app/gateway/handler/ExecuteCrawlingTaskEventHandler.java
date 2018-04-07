package ua.dudka.webcrawler.executor.app.gateway.handler;

import ua.dudka.webcrawler.executor.domain.event.ExecuteCrawlingTaskEvent;

public interface ExecuteCrawlingTaskEventHandler {

    void handle(ExecuteCrawlingTaskEvent task);
}