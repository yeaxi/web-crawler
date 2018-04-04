package ua.dudka.webcrawler.client.app.env;

import ua.dudka.webcrawler.client.app.env.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;

public interface ExecuteCrawlingTaskEventSender {
    void sendForExecution(ExecuteCrawlingTaskEvent event);
}
