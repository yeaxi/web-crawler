package ua.dudka.webcrawler.client.domain.service;


import org.junit.Before;
import org.junit.Test;
import ua.dudka.webcrawler.client.app.env.CrawlingTaskExecutorSender;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.model.StartPage;
import ua.dudka.webcrawler.client.domain.service.impl.DefaultCrawlingTaskScheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CrawlingTaskSchedulerTest {

    private ScheduledExecutorService mockExecutor = mock(ScheduledExecutorService.class);
    private Map<CrawlingTask, ScheduledFuture> tasks = new HashMap<>();
    private CrawlingTaskExecutorSender sender = mock(CrawlingTaskExecutorSender.class);

    private CrawlingTaskScheduler scheduler = new DefaultCrawlingTaskScheduler(mockExecutor, tasks, sender);

    private ScheduledFuture<?> mockFuture = mock(ScheduledFuture.class);

    @Before
    public void setUp() throws Exception {
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
    public void scheduleExecutionShouldSendTaskForExecution() throws Exception {
        CrawlingTask task = new CrawlingTask(StartPage.of("page"), now());

        scheduler.scheduleExecution(task);

        verify(mockExecutor).schedule(any(Runnable.class), eq(countDelayInSeconds(task.getStartTime())), eq(TimeUnit.SECONDS));
        verify(sender).sendForExecution(eq(task));
    }

    private long countDelayInSeconds(LocalDateTime startTime) {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), startTime);
    }

    @Test
    public void scheduleExecutionShouldAddTaskToContainer() {
        CrawlingTask task = new CrawlingTask(StartPage.of("page"), now());

        scheduler.scheduleExecution(task);
        ScheduledFuture<?> future = tasks.get(task);

        assertThat(tasks.containsKey(task)).isTrue();
        assertEquals(mockFuture, future); //did't use assertJ because of ambiguous method call
    }

    @Test
    public void cancelSchedulingShouldCancelFutureByTask() {
        CrawlingTask task = new CrawlingTask(StartPage.of("page"), now().plusMinutes(1));
        scheduler.scheduleExecution(task);

        scheduler.cancelScheduling(task);

        assertThat(tasks.get(task).isCancelled()).isTrue();
    }
}