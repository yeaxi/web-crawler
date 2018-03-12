package ua.dudka.webcrawler.client.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.model.vo.StartPage;
import ua.dudka.webcrawler.client.domain.service.CrawlingTaskService;
import ua.dudka.webcrawler.client.web.request.CreateCrawlingTaskRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CrawlingTaskControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private CrawlingTaskService crawlingTaskService;

    private StartPage startPage = StartPage.from("https://google.com", "google");
    private CrawlingTask task = new CrawlingTask(startPage, LocalDateTime.now(), Duration.of(1, ChronoUnit.MINUTES));

    @Test
    public void getAllTasks() {
    }

    @Test
    public void addTask() {
        CreateCrawlingTaskRequest request = new CreateCrawlingTaskRequest("google.com", "google", LocalDateTime.now(), Duration.ZERO);
        webClient.post()
                .uri("/tasks")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), CreateCrawlingTaskRequest.class)
                .exchange()
                .expectStatus().isOk();

        verify(crawlingTaskService, only()).addCrawlingTask(eq(request));
    }

    @Test
    public void removeTask() {
    }
}