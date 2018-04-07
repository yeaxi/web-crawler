package ua.dudka.webcrawler.executor.app.gateway.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import ua.dudka.webcrawler.executor.domain.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.service.CrawlingTaskExecutor;
import ua.dudka.webcrawler.executor.app.gateway.handler.ExecuteCrawlingTaskEventHandler;
import ua.dudka.webcrawler.executor.app.config.CrawlingTaskStreams;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultExecuteCrawlingTaskEventHandler implements ExecuteCrawlingTaskEventHandler {
    private final CrawlingTaskExecutor executor;

    @StreamListener(CrawlingTaskStreams.EXECUTE_TASK_INPUT_STREAM)
    @Override
    public void handle(ExecuteCrawlingTaskEvent task) {
        log.info("Handling {}", task);
        executor.execute(task);
    }
}