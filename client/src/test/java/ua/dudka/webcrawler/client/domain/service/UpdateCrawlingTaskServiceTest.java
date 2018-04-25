package ua.dudka.webcrawler.client.domain.service;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.gateway.ExecuteCrawlingTaskEventSender;
import ua.dudka.webcrawler.client.app.gateway.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.client.app.gateway.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.client.app.gateway.impl.DummyExecuteCrawlingTaskEventSender;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.service.impl.DefaultUpdateCrawlingTaskService;
import ua.dudka.webcrawler.client.exception.ReachedMaxVisitedLinksException;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UpdateCrawlingTaskServiceTest {
    private static final String VISITED_LINK = "com.com";
    private static final String EXISTENT_ID = "43534534234";

    private CrawlingTaskRepository repository = mock(CrawlingTaskRepository.class);
    private ExecuteCrawlingTaskEventSender sender = spy(DummyExecuteCrawlingTaskEventSender.class);
    private UpdateCrawlingTaskService service = new DefaultUpdateCrawlingTaskService(repository, sender);

    private CrawlingTask crawlingTask = new CrawlingTask("", 1, LocalDateTime.now());

    @Rule
    public ReachedMaxVisitedLinksException exception;


    @BeforeEach
    void setUp() {
        when(repository.findById(eq(EXISTENT_ID))).thenReturn(Mono.just(crawlingTask));
        when(repository.save(any(CrawlingTask.class))).thenAnswer(a -> Mono.just(a.getArgument(0)));
    }

    @Test
    void handleEventWithExistentIdShouldSaveUpdatedTask() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        service.updateTask(event).subscribe();

        verify(repository).save(crawlingTask);
    }

    @Test
    void handleEventWithExistentIdShouldSendForExecutionUpdatedTask() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        service.updateTask(event).subscribe();

        verify(sender).sendForExecution(eq(new ExecuteCrawlingTaskEvent(event.getTaskId(), event.getVisitedLink())));
    }

    @Test
    void handleEventWithExistentIdShouldUpdateCrawlingTask() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent(EXISTENT_ID, VISITED_LINK);
        service.updateTask(event).subscribe();

        assertThat(crawlingTask.getVisitedLinks()).containsOnly(VISITED_LINK);
    }

    @Test
    void givenCrawlingTaskWithMaxVisitedLinks_WhenNewUpdateEventComes_Then_TaskShouldNotBeSavedToDB() {
        CrawlingTask task = new CrawlingTask("", 1, LocalDateTime.now());
        task.addVisitedLink("link");
        when(repository.findById("1")).thenReturn(Mono.just(task));
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent("1", VISITED_LINK);


        service.updateTask(event).subscribe();

        verify(repository, never()).save(any(CrawlingTask.class));
    }

    @Test
    void givenCrawlingTaskWithMaxVisitedLinks_WhenNewUpdateEventComes_Then_TaskShouldNotBeSentToExecution() {
        CrawlingTask task = new CrawlingTask("", 1, LocalDateTime.now());
        task.addVisitedLink("link");
        when(repository.findById("1")).thenReturn(Mono.just(task));
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent("1", VISITED_LINK);


        service.updateTask(event).subscribe();

        verify(sender, never()).sendForExecution(any(ExecuteCrawlingTaskEvent.class));

    }


    @Test
    void givenCrawlingTaskWithMaxVisitedLinks_WhenNewUpdateEventComes_Then_TaskShouldNotContainNewVisitedLink() {
        CrawlingTask task = new CrawlingTask("", 1, LocalDateTime.now());
        task.addVisitedLink("link");
        when(repository.findById("1")).thenReturn(Mono.just(task));
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent("1", VISITED_LINK);


        service.updateTask(event).subscribe();

        assertThat(task.getVisitedLinks()).containsOnly("link");
    }

    @Test
    void updateNonexistentTaskShouldNotCallRepository() {
        when(repository.findById("1")).thenReturn(Mono.empty());
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent("1", VISITED_LINK);

        service.updateTask(event).subscribe();

        verify(repository, never()).save(any());
    }

    @Test
    void updateNonexistentTaskShouldNotSendNewTaskForExecution() {
        when(repository.findById("1")).thenReturn(Mono.empty());
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent("1", VISITED_LINK);

        service.updateTask(event).subscribe();

        verify(sender, never()).sendForExecution(any(ExecuteCrawlingTaskEvent.class));

    }
}