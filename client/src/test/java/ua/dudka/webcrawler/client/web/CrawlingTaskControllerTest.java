package ua.dudka.webcrawler.client.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.service.CrawlingTaskService;
import ua.dudka.webcrawler.client.exception.TaskNotFoundException;
import ua.dudka.webcrawler.client.web.dto.CreateCrawlingTaskRequest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static ua.dudka.webcrawler.client.web.CrawlingTaskController.Links.TASKS_PATH;
import static ua.dudka.webcrawler.client.web.CrawlingTaskController.Links.TASK_PATH;

class CrawlingTaskControllerTest {

    private CrawlingTaskService crawlingTaskService = mock(CrawlingTaskService.class);

    private WebTestClient webClient = WebTestClient.bindToController(new CrawlingTaskController(crawlingTaskService), new CrawlingTaskController.CrawlingTaskExceptionHandler())
            .configureClient()
            .build();

    @Test
    void getAllTasksShouldReturnTasksFromService() {
        CrawlingTask task = new CrawlingTask("https://google.com", 1, LocalDateTime.now());
        when(crawlingTaskService.findAll()).thenReturn(Flux.just(task));

        webClient.get()
                .uri(TASKS_PATH)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrawlingTask.class)
                .contains(task);
    }

    @Test
     void addTaskShouldAddItToService() {
        CreateCrawlingTaskRequest request = new CreateCrawlingTaskRequest("google.com", LocalDateTime.now(), 100);

        webClient.post()
                .uri(TASKS_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), CreateCrawlingTaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CrawlingTask.class);

        verify(crawlingTaskService, only()).addTask(eq(request));
    }

    @Test
     void removeExistentTaskShouldReturnOk() {
        String existentTaskId = "P2f45";
        when(crawlingTaskService.removeTask(eq(existentTaskId))).thenReturn(Mono.empty());

        webClient.delete()
                .uri(TASK_PATH, existentTaskId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk();

        verify(crawlingTaskService, only()).removeTask(eq(existentTaskId));
    }

    @Test
     void removeNonexistentTaskShouldReturn404k() {
        String nonexistentTaskId = "40sdg";
        doThrow(new TaskNotFoundException(nonexistentTaskId))
                .when(crawlingTaskService).removeTask(eq(nonexistentTaskId));

        webClient.delete()
                .uri(TASK_PATH, nonexistentTaskId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound();
    }
}