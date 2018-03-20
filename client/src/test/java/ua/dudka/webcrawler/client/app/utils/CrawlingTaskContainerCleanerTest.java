package ua.dudka.webcrawler.client.app.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.model.StartPage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CrawlingTaskContainerCleanerTest {

    private Map<CrawlingTask, ScheduledFuture> tasks = new HashMap<>();
    private CrawlingTask scheduledTask = new CrawlingTask(StartPage.of("p"), LocalDateTime.now());
    private CrawlingTask canceledTask = new CrawlingTask(StartPage.of("p"), LocalDateTime.now());
    private ScheduledFuture scheduledFuture = mock(ScheduledFuture.class);
    private ScheduledFuture canceledFuture = mock(ScheduledFuture.class);
    private CrawlingTaskContainerCleaner cleaner = new ScheduledCrawlingTaskContainerCleaner(tasks);

    @BeforeEach
    void setUp() {
        when(canceledFuture.isCancelled()).thenReturn(true);
        tasks.put(canceledTask, canceledFuture);
        tasks.put(scheduledTask, scheduledFuture);
    }

    @AfterEach
    void tearDown() {
        tasks.clear();
    }

    @Test
    void cleanShouldRemoveCanceledTasksFromContainer() {
        cleaner.clean();

        assertThat(tasks.containsKey(canceledTask)).isFalse();
        assertThat(tasks.containsValue(canceledFuture)).isFalse();
    }

    @Test
    void cleanShouldLeaveNOTCanceledTasksFromContainer() {
        cleaner.clean();

        assertThat(tasks.containsKey(scheduledTask)).isTrue();
        assertThat(tasks.containsValue(scheduledFuture)).isTrue();
    }
}