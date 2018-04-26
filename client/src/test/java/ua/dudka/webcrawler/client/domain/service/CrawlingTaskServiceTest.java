package ua.dudka.webcrawler.client.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.model.ExecutionStatus;
import ua.dudka.webcrawler.client.domain.service.impl.DefaultCrawlingTaskService;
import ua.dudka.webcrawler.client.exception.TaskNotFoundException;
import ua.dudka.webcrawler.client.repository.CrawlingTaskRepository;
import ua.dudka.webcrawler.client.web.dto.CreateCrawlingTaskRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CrawlingTaskServiceTest {

    private CrawlingTaskScheduler scheduler = mock(CrawlingTaskScheduler.class);
    private CrawlingTaskRepository repository = mock(CrawlingTaskRepository.class);
    private CrawlingTaskService service = new DefaultCrawlingTaskService(repository, scheduler);


    @BeforeEach
    void setUp() throws Exception {
        when(repository.save(any(CrawlingTask.class))).then(invocation -> Mono.just(invocation.getArgument(0)));
    }

    @Test
    void addTaskShouldCreateCrawlingTaskFromRequest() {
        CreateCrawlingTaskRequest request = new CreateCrawlingTaskRequest("testPage", LocalDateTime.now(), 1);

        CrawlingTask task = service.addTask(request).block();

        assertThat(task.getId()).isNotBlank();
        assertThat(task.getStartPage()).isEqualTo(request.getUrl());
        assertThat(task.getMaxVisitedLinks()).isEqualTo(request.getMaxVisitedLinks());
        assertThat(task.getStartTime()).isEqualTo(request.getStartTime());
        assertThat(task.getExecutionStatus()).isEqualTo(ExecutionStatus.SCHEDULED);
    }

    @Test
    void addTaskShouldSaveItAndSendToScheduler() {
        CreateCrawlingTaskRequest request = new CreateCrawlingTaskRequest("testPage", LocalDateTime.now(), 1);

        service.addTask(request).block();

        verify(repository, only()).save(any(CrawlingTask.class));
        verify(scheduler, only()).scheduleExecution(any(CrawlingTask.class));
    }

    @Test
    void findAllShouldCallRepository() {
        service.findAll();
        verify(repository, only()).findAll();
    }

    @Test
    void removeExistentTaskShouldDeleteItFromRepo() {
        CrawlingTask task = new CrawlingTask();
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(repository).findById(task.getId());
        verify(repository).delete(task);
    }

    @Test
    void removeNonexistentTaskShouldThrowNotFoundException() {
        String id = "nonexistent id";
        when(repository.findById(id)).thenReturn(Mono.empty());

        assertThrows(TaskNotFoundException.class, () -> service.removeTask(id).block());
    }

    @Test
    void removeIdleTaskShouldCancelScheduling() {
        CrawlingTask task = new CrawlingTask();
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(scheduler, only()).cancelScheduling(eq(task));
    }

    @Test
    void removeRunningTaskShouldNOTCancelScheduling() {
        CrawlingTask task = new CrawlingTask("testPage", 1, LocalDateTime.now(), ExecutionStatus.RUNNING);
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(scheduler, never()).cancelScheduling(eq(task));

    }

    @Test
    void removeCompletedTaskShouldNOTCancelScheduling() {
        CrawlingTask task = new CrawlingTask("testPage", 1, LocalDateTime.now(), ExecutionStatus.COMPLETED);
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(scheduler, never()).cancelScheduling(eq(task));

    }

    @Test
    void removeFailedTaskShouldNOTCancelScheduling() {
        CrawlingTask task = new CrawlingTask("testPage", 1, LocalDateTime.now(), ExecutionStatus.ERROR);
        when(repository.findById(task.getId())).thenReturn(Mono.just(task));

        service.removeTask(task.getId()).block();

        verify(scheduler, never()).cancelScheduling(eq(task));

    }
}