package ua.dudka.webcrawler.client.app.env;

import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.env.event.UpdateCrawlingTaskEvent;

public interface UpdateCrawlingTaskEventHandler {

    void handle(UpdateCrawlingTaskEvent event);
}