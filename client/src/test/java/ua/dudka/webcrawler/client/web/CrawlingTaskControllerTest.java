package ua.dudka.webcrawler.client.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.service.CrawlingTaskService;
import ua.dudka.webcrawler.client.web.dto.CreateCrawlingTaskRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static ua.dudka.webcrawler.client.web.CrawlingTaskController.Links.TASKS_RESOURCE;

public class CrawlingTaskControllerTest {


    private CrawlingTaskService crawlingTaskService = mock(CrawlingTaskService.class);

    private WebTestClient webClient = WebTestClient.bindToController(new CrawlingTaskController(crawlingTaskService))
            .configureClient()
            .baseUrl(TASKS_RESOURCE)
            .build();

    private CrawlingTask task = new CrawlingTask("https://google.com", LocalDateTime.now(), Duration.of(1, ChronoUnit.MINUTES));

    @Before
    public void setUp() throws Exception {
        when(crawlingTaskService.findAll()).thenReturn(Flux.just(task));
    }

    @Test
    public void getAllTasks() {
        webClient.get()
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CrawlingTask.class)
                .contains(task);
    }

    @Test
    public void addTask() {
        CreateCrawlingTaskRequest request = new CreateCrawlingTaskRequest("google.com", LocalDateTime.now(), Duration.ZERO);
        webClient.post()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), CreateCrawlingTaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CrawlingTask.class);

        verify(crawlingTaskService, only()).addTask(eq(request));
    }

    @Test
    public void removeTask() {
    }
}