package ua.dudka.webcrawler.client.app.env;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.env.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.client.app.env.impl.DefaultUpdateCrawlingTaskEventHandler;
import ua.dudka.webcrawler.client.app.env.impl.DummyCrawlingTaskExecutorSender;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UpdateCrawlingTaskEventHandlerTest {

    private static final String VISITED_LINK = "com.com";
    private static final String EXISTENT_ID = "2";

    private CrawlingTaskRepository repository = mock(CrawlingTaskRepository.class);
    private CrawlingTaskExecutorSender sender = spy(DummyCrawlingTaskExecutorSender.class);
    private UpdateCrawlingTaskEventHandler handler = new DefaultUpdateCrawlingTaskEventHandler(repository, sender);

    private CrawlingTask crawlingTask = new CrawlingTask("", 1, LocalDateTime.now());


    @BeforeEach
    void setUp() {
        when(repository.findById(eq(EXISTENT_ID))).thenReturn(Mono.just(crawlingTask));
        when(repository.save(any(CrawlingTask.class))).thenAnswer(a -> Mono.just(a.getArgument(0)));
    }

    @Test
    void handleEventWithExistentIdShouldSaveUpdatedTask() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        handler.handle(event).block();

        verify(repository).save(crawlingTask);
    }

    @Test
    void handleEventWithExistentIdShouldSendForExecutionUpdatedTask() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        handler.handle(event).block();

        verify(sender).sendForExecution(crawlingTask);
    }

    @Test
    void handleEventWithExistentIdShouldUpdateCrawlingTask() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        handler.handle(event).block();

        assertThat(crawlingTask.getVisitedLinks()).containsOnly(VISITED_LINK);
    }

    @Test
    void givenCrawlingTaskWithMaxVisitedLinks_WhenNewUpdateEventComes_Then_TaskShouldNotBeSavedToDB() {
        CrawlingTask task = new CrawlingTask("", 1, LocalDateTime.now());
        task.addVisitedLink("link");
        when(repository.findById("1")).thenReturn(Mono.just(task));
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent("1", VISITED_LINK);


        handler.handle(event);

        verify(repository, never()).save(eq(task));
    }

    @Test
    void givenCrawlingTaskWithMaxVisitedLinks_WhenNewUpdateEventComes_Then_TaskShouldNotBeSentToExecution() {
        CrawlingTask task = new CrawlingTask("", 1, LocalDateTime.now());
        task.addVisitedLink("link");
        when(repository.findById("1")).thenReturn(Mono.just(task));
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent("1", VISITED_LINK);


        handler.handle(event);

        verify(sender, never()).sendForExecution(eq(task));

    }


    @Test
    void givenCrawlingTaskWithMaxVisitedLinks_WhenNewUpdateEventComes_Then_TaskShouldNotContainNewVisitedLink() {
        CrawlingTask task = new CrawlingTask("", 1, LocalDateTime.now());
        task.addVisitedLink("link");
        when(repository.findById("1")).thenReturn(Mono.just(task));
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent("1", VISITED_LINK);


        handler.handle(event);

        assertThat(task.getVisitedLinks()).containsOnly("link");
    }
}