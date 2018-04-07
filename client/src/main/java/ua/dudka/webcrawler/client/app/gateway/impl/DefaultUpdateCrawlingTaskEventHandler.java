package ua.dudka.webcrawler.client.app.gateway.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.gateway.ExecuteCrawlingTaskEventSender;
import ua.dudka.webcrawler.client.app.gateway.UpdateCrawlingTaskEventHandler;
import ua.dudka.webcrawler.client.app.gateway.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.client.app.gateway.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;

@Component
@RequiredArgsConstructor
public class DefaultUpdateCrawlingTaskEventHandler implements UpdateCrawlingTaskEventHandler {
    private final CrawlingTaskRepository repository;
    private final ExecuteCrawlingTaskEventSender sender;

    @Override
    public Mono<Void> handle(UpdateCrawlingTaskEvent event) {
        return repository.findById(event.getTaskId())
                .map(task -> {
                    task.addVisitedLink(event.getVisitedLink());
                    return task;
                })
                .flatMap(repository::save)
                .map(t -> new ExecuteCrawlingTaskEvent(event.getTaskId(), event.getVisitedLink()))
                .doOnSuccess(sender::sendForExecution)
                .then();
    }
}