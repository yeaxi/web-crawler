package ua.dudka.webcrawler.client.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.service.CrawlingTaskService;
import ua.dudka.webcrawler.client.exception.TaskNotFoundException;
import ua.dudka.webcrawler.client.web.dto.CreateCrawlingTaskRequest;

import static ua.dudka.webcrawler.client.web.CrawlingTaskController.Links.TASKS_PATH;
import static ua.dudka.webcrawler.client.web.CrawlingTaskController.Links.TASK_PATH;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CrawlingTaskController {

    private final CrawlingTaskService taskService;

    @GetMapping(TASKS_PATH)
    public Flux<CrawlingTask> findAllTasks() {
        return taskService.findAll();
    }

    @PostMapping(TASKS_PATH)
    public Mono<CrawlingTask> addTask(@RequestBody CreateCrawlingTaskRequest request) {
        log.info("Processing {}", request);
        return taskService.addTask(request);
    }

    @DeleteMapping(TASK_PATH)
    public Mono<Void> deleteTask(@PathVariable("id") String taskId) {
        log.info("Removing task with id {}", taskId);
        return taskService.removeTask(taskId);
    }


    public static class Links {
        public static final String TASKS_PATH = "tasks";
        public static final String TASK_PATH = TASKS_PATH + "/{id}";
    }

    @RestControllerAdvice
    public static class CrawlingTaskExceptionHandler {

        @ResponseStatus(HttpStatus.NOT_FOUND)
        @ExceptionHandler(TaskNotFoundException.class)
        public String handle(TaskNotFoundException e) {
            return e.getMessage();
        }
    }
}