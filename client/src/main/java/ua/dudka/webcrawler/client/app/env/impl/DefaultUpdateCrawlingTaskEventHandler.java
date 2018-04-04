package ua.dudka.webcrawler.client.app.env.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.env.CrawlingTaskExecutorSender;
import ua.dudka.webcrawler.client.app.env.UpdateCrawlingTaskEventHandler;
import ua.dudka.webcrawler.client.app.env.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;

@Component
@RequiredArgsConstructor
public class DefaultUpdateCrawlingTaskEventHandler implements UpdateCrawlingTaskEventHandler {
    private final CrawlingTaskRepository repository;
    private final CrawlingTaskExecutorSender sender;

    @Override
    public Mono<Void> handle(UpdateCrawlingTaskEvent event) {
        return repository.findById(event.getTaskId())
                .map(task -> {
                    task.addVisitedLink(event.getVisitedLink());
                    return task;
                })
                .flatMap(repository::save)
                .doOnSuccess(sender::sendForExecution)
                .then();
    }
}