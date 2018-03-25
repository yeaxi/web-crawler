package ua.dudka.webcrawler.executor.domain.service;

import org.junit.jupiter.api.Test;
import ua.dudka.webcrawler.executor.domain.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.service.impl.DefaultCrawlingTaskExecutor;
import ua.dudka.webcrawler.executor.env.publisher.UpdateCrawlingTaskEventPublisher;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CrawlingTaskExecutorTest {

    private WebPageParser mockParser = mock(WebPageParser.class);
    private UpdateCrawlingTaskEventPublisher mockPublisher = mock(UpdateCrawlingTaskEventPublisher.class);
    private CrawlingTaskExecutor executor = new DefaultCrawlingTaskExecutor(mockParser, mockPublisher);
    private ExecuteCrawlingTaskEvent event = new ExecuteCrawlingTaskEvent("1", "comcom");

    @Test
    void executeShouldPublishUpdateEventForEachLink() {
        Set<String> parsedLinks = Set.of("1", "2", "3", "4");
        when(mockParser.parse(eq(event.getLink()))).thenReturn(parsedLinks);

        executor.execute(event);

        verify(mockPublisher, times(parsedLinks.size()))
                .publishEvent(isA(UpdateCrawlingTaskEvent.class));
    }
}