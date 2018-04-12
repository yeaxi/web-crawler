package ua.dudka.webcrawler.client.app.gateway;

import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.gateway.event.UpdateCrawlingTaskEvent;

public interface UpdateCrawlingTaskEventHandler {

    void handle(UpdateCrawlingTaskEvent event);
}
