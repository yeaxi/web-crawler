package ua.dudka.webcrawler.client.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.service.CrawlingTaskService;
import ua.dudka.webcrawler.client.web.dto.CreateCrawlingTaskRequest;

import static ua.dudka.webcrawler.client.web.CrawlingTaskController.Links.TASKS_RESOURCE;

@RestController
@RequestMapping(path = TASKS_RESOURCE)
@RequiredArgsConstructor
@Slf4j
public class CrawlingTaskController {

    private final CrawlingTaskService taskService;

    @PostMapping
    public Mono<CrawlingTask> addTask(@RequestBody CreateCrawlingTaskRequest request) {
        log.debug("Processing {}", request);
        return taskService.addTask(request);
    }


    @GetMapping
    public Flux<CrawlingTask> findAllTasks() {
        return taskService.findAll();
    }

    public static class Links {
        public static final String TASKS_RESOURCE = "tasks";
    }
}