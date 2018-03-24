package ua.dudka.webcrawler.executor.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dudka.webcrawler.executor.domain.model.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.model.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.service.CrawlingTaskExecutor;
import ua.dudka.webcrawler.executor.domain.service.WebPageParser;
import ua.dudka.webcrawler.executor.env.publisher.UpdateCrawlingTaskEventPublisher;

@Service
@RequiredArgsConstructor
public class DefaultCrawlingTaskExecutor implements CrawlingTaskExecutor {
    private final WebPageParser mockParser;
    private final UpdateCrawlingTaskEventPublisher mockPublisher;

    @Override
    public void execute(ExecuteCrawlingTaskEvent event) {
        mockParser.parse(event.getLink())
                .parallelStream()
                .map(link -> new UpdateCrawlingTaskEvent(event.getId(), link))
                .forEach(mockPublisher::publishEvent);
    }
}