package ua.dudka.webcrawler.client.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.service.CrawlingTaskService;
import ua.dudka.webcrawler.client.web.request.CreateCrawlingTaskRequest;

@RestController
@RequestMapping(path = "/tasks")
@RequiredArgsConstructor
@Slf4j
public class CrawlingTaskController {

    private final CrawlingTaskService taskService;

    @PostMapping
    public Mono<CrawlingTask> addTask(@RequestBody CreateCrawlingTaskRequest request) {
        log.debug("Processing {}", request);
        return taskService.addCrawlingTask(request);
    }
}