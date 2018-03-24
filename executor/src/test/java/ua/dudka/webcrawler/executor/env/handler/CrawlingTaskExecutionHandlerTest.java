package ua.dudka.webcrawler.executor.env.handler;

import org.junit.jupiter.api.Test;
import ua.dudka.webcrawler.executor.domain.model.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.service.CrawlingTaskExecutor;
import ua.dudka.webcrawler.executor.env.handler.impl.DefaultCrawlingTaskExecutionHandler;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CrawlingTaskExecutionHandlerTest {

    @Test
    void handleExecuteCrawlingTaskEventShouldSendItForExecution() {
        CrawlingTaskExecutor executor = mock(CrawlingTaskExecutor.class);
        CrawlingTaskExecutionHandler handler = new DefaultCrawlingTaskExecutionHandler(executor);

        ExecuteCrawlingTaskEvent task = new ExecuteCrawlingTaskEvent("1", "link");
        handler.handle(task);

        verify(executor).execute(eq(task));
    }
}