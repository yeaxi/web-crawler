package ua.dudka.webcrawler.client.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.gateway.ExecuteCrawlingTaskEventSender;
import ua.dudka.webcrawler.client.app.gateway.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.client.app.gateway.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.client.domain.service.UpdateCrawlingTaskService;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;

@Service
@RequiredArgsConstructor
public class DefaultUpdateCrawlingTaskService implements UpdateCrawlingTaskService {
    private final CrawlingTaskRepository repository;
    private final ExecuteCrawlingTaskEventSender sender;

    @Override
    public Mono<Void> updateTask(UpdateCrawlingTaskEvent event) {
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