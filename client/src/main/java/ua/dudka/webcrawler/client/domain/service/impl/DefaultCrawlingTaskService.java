package ua.dudka.webcrawler.client.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.env.CrawlingTaskScheduler;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.model.ExecutionStatus;
import ua.dudka.webcrawler.client.domain.service.CrawlingTaskService;
import ua.dudka.webcrawler.client.exception.TaskNotFoundException;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;
import ua.dudka.webcrawler.client.web.dto.CreateCrawlingTaskRequest;

@Service
@RequiredArgsConstructor
public class DefaultCrawlingTaskService implements CrawlingTaskService {
    private final CrawlingTaskRepository repository;
    private final CrawlingTaskScheduler scheduler;

    @Override
    public Mono<CrawlingTask> addTask(CreateCrawlingTaskRequest request) {
        CrawlingTask task = new CrawlingTask(request.getUrl(), request.getStartTime(), request.getDuration());
        return repository.save(task).doOnSuccess(scheduler::scheduleExecution);
    }

    @Override
    public Flux<CrawlingTask> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Void> removeTask(String taskId) {
        return repository.findById(taskId)
                .doOnSuccess(this::checkForEmpty)
                .doOnSuccess(this::cancelIfIdle)
                .doOnSuccess(repository::delete)
                .then().log();
    }

    private void checkForEmpty(CrawlingTask ct) {
        if (ct == null) {
            throw new TaskNotFoundException("1234");
        }
    }

    private void cancelIfIdle(CrawlingTask task) {
        if (task.getExecutionStatus() == ExecutionStatus.IDLE) {
            scheduler.cancelScheduling(task);
        }
    }
}