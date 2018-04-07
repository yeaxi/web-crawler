package ua.dudka.webcrawler.client.domain.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.dudka.webcrawler.client.app.gateway.ExecuteCrawlingTaskEventSender;
import ua.dudka.webcrawler.client.app.gateway.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.service.impl.DefaultCrawlingTaskScheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CrawlingTaskSchedulerTest {

    private ScheduledExecutorService mockExecutor = mock(ScheduledExecutorService.class);
    private Map<CrawlingTask, ScheduledFuture> tasks = new HashMap<>();
    private ExecuteCrawlingTaskEventSender sender = mock(ExecuteCrawlingTaskEventSender.class);

    private CrawlingTaskScheduler scheduler = new DefaultCrawlingTaskScheduler(mockExecutor, tasks, sender);

    private ScheduledFuture<?> mockFuture = mock(ScheduledFuture.class);

    @BeforeEach
    void setUp() throws Exception {
        when(mockExecutor.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class))).thenAnswer(invocation -> {
            Thread.sleep(invocation.getArgument(1));
            ((Runnable) invocation.getArgument(0)).run();
            return mockFuture;
        });
        when(mockFuture.cancel(anyBoolean())).then(invocation -> {
            when(mockFuture.isCancelled()).thenReturn(true);
            return true;
        });

    }

    @Test
    void scheduleExecutionShouldSendTaskForExecution() throws Exception {
        CrawlingTask task = new CrawlingTask();

        scheduler.scheduleExecution(task);

        verify(mockExecutor).schedule(any(Runnable.class), eq(countDelayInSeconds(task.getStartTime())), eq(TimeUnit.SECONDS));
        verify(sender).sendForExecution(eq(new ExecuteCrawlingTaskEvent(task.getId(), task.getStartPage())));
    }

    private long countDelayInSeconds(LocalDateTime startTime) {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), startTime);
    }

    @Test
    void scheduleExecutionShouldAddTaskToContainer() {
        CrawlingTask task = new CrawlingTask();

        scheduler.scheduleExecution(task);
        ScheduledFuture<?> future = tasks.get(task);

        assertThat(tasks.containsKey(task)).isTrue();
        assertEquals(mockFuture, future); //did't use assertJ because of ambiguous method call
    }

    @Test
    void cancelSchedulingShouldCancelAndRemoveTask() {
        CrawlingTask task = new CrawlingTask();
        scheduler.scheduleExecution(task);
        ScheduledFuture future = tasks.get(task);

        scheduler.cancelScheduling(task);

        assertThat(future.isCancelled()).isTrue();
        assertThat(tasks.containsKey(task)).isFalse();
    }
}