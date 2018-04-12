package ua.dudka.webcrawler.client.app.gateway.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.gateway.UpdateCrawlingTaskEventHandler;
import ua.dudka.webcrawler.client.app.gateway.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.client.domain.service.UpdateCrawlingTaskService;

import static ua.dudka.webcrawler.client.app.config.CrawlingTaskStreams.UPDATE_TASK_INPUT_STREAM;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultUpdateCrawlingTaskEventHandler implements UpdateCrawlingTaskEventHandler {

    private final UpdateCrawlingTaskService service;

    @StreamListener(UPDATE_TASK_INPUT_STREAM)
    @Override
    public void handle(UpdateCrawlingTaskEvent event) {
        log.info("handling {}", event);
        service.updateTask(event);
    }
}