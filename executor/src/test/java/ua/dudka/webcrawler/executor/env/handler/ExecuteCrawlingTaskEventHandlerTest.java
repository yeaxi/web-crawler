package ua.dudka.webcrawler.executor.env.handler;

import org.junit.jupiter.api.Test;
import ua.dudka.webcrawler.executor.domain.event.ExecuteCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.domain.service.CrawlingTaskExecutor;
import ua.dudka.webcrawler.executor.env.handler.impl.DefaultExecuteCrawlingTaskEventHandler;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ExecuteCrawlingTaskEventHandlerTest {

    @Test
    void handleExecuteCrawlingTaskEventShouldSendItForExecution() {
        CrawlingTaskExecutor executor = mock(CrawlingTaskExecutor.class);
        ExecuteCrawlingTaskEventHandler handler = new DefaultExecuteCrawlingTaskEventHandler(executor);

        ExecuteCrawlingTaskEvent task = new ExecuteCrawlingTaskEvent("1", "link");
        handler.handle(task);

        verify(executor).execute(eq(task));
    }
}