package ua.dudka.webcrawler.client.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.gateway.ExecuteCrawlingTaskEventSender;
import ua.dudka.webcrawler.client.app.gateway.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.client.app.gateway.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.service.UpdateCrawlingTaskService;
import ua.dudka.webcrawler.client.exception.TaskNotFoundException;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUpdateCrawlingTaskService implements UpdateCrawlingTaskService {
    private final CrawlingTaskRepository repository;
    private final ExecuteCrawlingTaskEventSender sender;

    @Override
    public Mono<Void> updateTask(UpdateCrawlingTaskEvent event) {
        return repository.findById(event.getTaskId())
                .doOnSuccess(t -> checkForEmpty(t, event.getTaskId()))
                .flatMap(task -> addVisitedLink(event, task))
                .doOnSuccess(t -> sendForExecution(event))
                .doOnError(throwable -> log.info(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.empty())
                .then();
    }

    private CrawlingTask checkForEmpty(CrawlingTask task, String id) {
        if (task == null)
            throw new TaskNotFoundException(id);

        return task;
    }

    private Mono<CrawlingTask> addVisitedLink(UpdateCrawlingTaskEvent event, CrawlingTask task) {
        log.info("adding link {} to the {}", event.getVisitedLink(), task);

        task.addVisitedLink(event.getVisitedLink());
        return repository.save(task);
    }

    private void sendForExecution(UpdateCrawlingTaskEvent event) {
        ExecuteCrawlingTaskEvent executeEvent = new ExecuteCrawlingTaskEvent(event.getTaskId(), event.getVisitedLink());

        log.info("sending {}", executeEvent);

        sender.sendForExecution(executeEvent);
    }
}