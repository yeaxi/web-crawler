package ua.dudka.webcrawler.executor.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dudka.webcrawler.executor.domain.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.service.CrawlingTaskExecutor;
import ua.dudka.webcrawler.executor.domain.service.WebPageParser;
import ua.dudka.webcrawler.executor.app.gateway.publisher.UpdateCrawlingTaskEventPublisher;

@Service
@RequiredArgsConstructor
public class DefaultCrawlingTaskExecutor implements CrawlingTaskExecutor {

    private final WebPageParser webPageParser;
    private final UpdateCrawlingTaskEventPublisher updateCrawlingTaskEventPublisher;

    @Override
    public void execute(ExecuteCrawlingTaskEvent event) {
        webPageParser.parse(event.getLink())
                .parallelStream()
                .map(link -> new UpdateCrawlingTaskEvent(event.getId(), link))
                .forEach(updateCrawlingTaskEventPublisher::publishEvent);
    }
}