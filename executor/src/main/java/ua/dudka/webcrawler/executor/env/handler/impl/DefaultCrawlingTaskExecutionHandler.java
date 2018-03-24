package ua.dudka.webcrawler.executor.env.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import ua.dudka.webcrawler.executor.domain.model.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.service.CrawlingTaskExecutor;
import ua.dudka.webcrawler.executor.env.handler.CrawlingTaskExecutionHandler;
import ua.dudka.webcrawler.executor.env.stream.CrawlingTaskStreams;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultCrawlingTaskExecutionHandler implements CrawlingTaskExecutionHandler {
    private final CrawlingTaskExecutor executor;

    @StreamListener(CrawlingTaskStreams.EXECUTE_TASK_INPUT_STREAM)
    @Override
    public void handle(ExecuteCrawlingTaskEvent task) {
        log.info("Handling {}", task);
        executor.execute(task);
    }
}