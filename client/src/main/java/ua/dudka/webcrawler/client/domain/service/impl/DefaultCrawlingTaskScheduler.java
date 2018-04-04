package ua.dudka.webcrawler.client.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.dudka.webcrawler.client.app.env.ExecuteCrawlingTaskEventSender;
import ua.dudka.webcrawler.client.app.env.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.service.CrawlingTaskScheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DefaultCrawlingTaskScheduler implements CrawlingTaskScheduler {
    private final ScheduledExecutorService executorService;
    private final Map<CrawlingTask, ScheduledFuture> tasks;
    private final ExecuteCrawlingTaskEventSender sender;


    @Override
    public void scheduleExecution(CrawlingTask task) {
        ExecuteCrawlingTaskEvent event = new ExecuteCrawlingTaskEvent(task.getId(), task.getStartPage());
        ScheduledFuture<?> future = executorService.schedule(() -> sender.sendForExecution(event),
                delayFrom(task.getStartTime()), TimeUnit.SECONDS);
        tasks.put(task, future);
    }

    private long delayFrom(LocalDateTime startTime) {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), startTime);
    }

    @Override
    public void cancelScheduling(CrawlingTask task) {
        tasks.get(task).cancel(false);
        tasks.remove(task);
    }
}