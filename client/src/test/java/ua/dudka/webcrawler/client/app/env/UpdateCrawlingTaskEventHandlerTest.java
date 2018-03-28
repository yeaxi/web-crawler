package ua.dudka.webcrawler.client.app.env;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.env.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.client.app.env.impl.DefaultUpdateCrawlingTaskEventHandler;
import ua.dudka.webcrawler.client.app.env.impl.DummyCrawlingTaskExecutorSender;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.model.StartPage;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UpdateCrawlingTaskEventHandlerTest {

    private static final String VISITED_LINK = "com.com";
    private static final String EXISTENT_ID = "2";

    private CrawlingTaskRepository repository = mock(CrawlingTaskRepository.class);
    private CrawlingTaskExecutorSender sender = mock(DummyCrawlingTaskExecutorSender.class);
    private UpdateCrawlingTaskEventHandler handler = new DefaultUpdateCrawlingTaskEventHandler(repository, sender);

    private CrawlingTask crawlingTask = new CrawlingTask(StartPage.of("start_link.com"), LocalDateTime.now());


    @BeforeEach
    void setUp() {
        when(repository.findById(eq(EXISTENT_ID))).thenReturn(Mono.just(crawlingTask));
    }

    @Test
    void handleEventWithExistentIdShouldSaveUpdatedTask() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        handler.handle(event);

        verify(repository).save(crawlingTask);
    }

    @Test
    void handleEventWithExistentIdShouldSendForExecutionUpdatedTask() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        handler.handle(event);

        verify(sender).sendForExecution(crawlingTask);
    }

    @Test
    void handleEventWithExistentIdShouldUpdateCrawlingTask() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        handler.handle(event);

        assertThat(crawlingTask.getStartPage().getVisitedLinks()).containsOnly(VISITED_LINK);
    }

    @Test
    void givenCrawlingTaskWithMaxVisitedLinks_WhenNewUpdateEventComes_Then_TaskShouldNotBeSavedToDB() {
        CrawlingTask task = new CrawlingTask(StartPage.of("", 1), LocalDateTime.now());
        task.addVisitedLink("link");
        when(repository.findById("1")).thenReturn(Mono.just(task));
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        handler.handle(event);

        assertThat(crawlingTask.getStartPage().getVisitedLinks()).containsOnly(VISITED_LINK);
    }
}