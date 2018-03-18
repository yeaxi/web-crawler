package ua.dudka.webcrawler.client.domain.service;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.env.CrawlingTaskScheduler;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.model.ExecutionStatus;
import ua.dudka.webcrawler.client.domain.service.impl.DefaultCrawlingTaskService;
import ua.dudka.webcrawler.client.exception.TaskNotFoundException;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;
import ua.dudka.webcrawler.client.web.dto.CreateCrawlingTaskRequest;

import java.time.LocalDateTime;

import static java.time.Duration.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CrawlingTaskServiceTest {

    private CrawlingTaskScheduler scheduler = mock(CrawlingTaskScheduler.class);
    private CrawlingTaskRepository repository = mock(CrawlingTaskRepository.class);
    private CrawlingTaskService service = new DefaultCrawlingTaskService(repository, scheduler);


    @Before
    public void setUp() throws Exception {
        when(repository.save(any(CrawlingTask.class))).then(invocation -> Mono.just(invocation.getArgument(0)));
    }

    @Test
    public void addTaskShouldCreateCrawlingTaskFromRequest() {
        CreateCrawlingTaskRequest request = new CreateCrawlingTaskRequest("testPage", LocalDateTime.now(), ZERO);

        CrawlingTask task = service.addTask(request).block();

        assertThat(task.getId()).isNotBlank();
        assertThat(task.getStartPage()).isEqualTo(request.getUrl());
        assertThat(task.getStartTime()).isEqualTo(request.getStartTime());
        assertThat(task.getDuration()).isEqualTo(request.getDuration());
        assertThat(task.getExecutionStatus()).isEqualTo(ExecutionStatus.IDLE);
    }

    @Test
    public void addTaskShouldSaveItAndSendToScheduler() {
        CreateCrawlingTaskRequest request = new CreateCrawlingTaskRequest("testPage", LocalDateTime.now(), ZERO);

        service.addTask(request).block();

        verify(repository, only()).save(any(CrawlingTask.class));
        verify(scheduler, only()).scheduleExecution(any(CrawlingTask.class));
    }

    @Test
    public void findAllShouldCallRepository() {
        service.findAll();
        verify(repository, only()).findAll();
    }

    @Test
    public void removeExistentTaskShouldDeleteItFromRepo() {
        CrawlingTask task = new CrawlingTask("testPage", LocalDateTime.now(), ZERO);
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(repository).findById(task.getId());
        verify(repository).delete(task);
    }

    @Test(expected = TaskNotFoundException.class)
    public void removeNonexistentTaskShouldThrowNotFoundException() {
        String id = "nonexistent id";
        when(repository.findById(id)).thenReturn(Mono.empty());

        service.removeTask(id).block();
    }

    @Test
    public void removeIdleTaskShouldCancelScheduling() {
        CrawlingTask task = new CrawlingTask("testPage", LocalDateTime.now(), ZERO);
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(scheduler, only()).cancelScheduling(eq(task));
    }

    @Test
    public void removeRunningTaskShouldNOTCancelScheduling() {
        CrawlingTask task = new CrawlingTask("testPage", LocalDateTime.now(), ZERO, ExecutionStatus.RUNNING);
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(scheduler, never()).cancelScheduling(eq(task));

    }

    @Test
    public void removeCompletedTaskShouldNOTCancelScheduling() {
        CrawlingTask task = new CrawlingTask("testPage", LocalDateTime.now(), ZERO, ExecutionStatus.COMPLETED);
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(scheduler, never()).cancelScheduling(eq(task));

    }

    @Test
    public void removeFailedTaskShouldNOTCancelScheduling() {
        CrawlingTask task = new CrawlingTask("testPage", LocalDateTime.now(), ZERO, ExecutionStatus.ERROR);
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(scheduler, never()).cancelScheduling(eq(task));

    }
}