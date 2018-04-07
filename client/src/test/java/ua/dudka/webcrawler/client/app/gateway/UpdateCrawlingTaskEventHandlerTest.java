package ua.dudka.webcrawler.client.app.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.app.gateway.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.client.app.gateway.impl.DefaultUpdateCrawlingTaskEventHandler;
import ua.dudka.webcrawler.client.domain.service.UpdateCrawlingTaskService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateCrawlingTaskEventHandlerTest {

    private UpdateCrawlingTaskService service = mock(UpdateCrawlingTaskService.class);
    private UpdateCrawlingTaskEventHandler handler = new DefaultUpdateCrawlingTaskEventHandler(service);

    @BeforeEach
    void setUp() {
        when(service.updateTask(any(UpdateCrawlingTaskEvent.class))).thenAnswer(a -> Mono.just(a.getArgument(0)));
    }

    @Test
    void handleEventShouldCallService() {
        UpdateCrawlingTaskEvent event = new UpdateCrawlingTaskEvent("id", "link");
        handler.handle(event).block();

        verify(service).updateTask(eq(event));
    }
}