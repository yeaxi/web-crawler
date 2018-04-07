package ua.dudka.webcrawler.client.domain.service;

import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.gateway.event.UpdateCrawlingTaskEvent;

public interface UpdateCrawlingTaskService {
    Mono<Void> updateTask(UpdateCrawlingTaskEvent event);
}