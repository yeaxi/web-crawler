package ua.dudka.webcrawler.client.app.gateway;

import ua.dudka.webcrawler.client.app.gateway.event.ExecuteCrawlingTaskEvent;

public interface ExecuteCrawlingTaskEventSender {
    void sendForExecution(ExecuteCrawlingTaskEvent event);
}
