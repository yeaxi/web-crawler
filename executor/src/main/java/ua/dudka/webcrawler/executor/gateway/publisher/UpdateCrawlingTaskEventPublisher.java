package ua.dudka.webcrawler.executor.gateway.publisher;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ua.dudka.webcrawler.executor.domain.event.UpdateCrawlingTaskEvent;
import ua.dudka.webcrawler.executor.gateway.config.CrawlingTaskStreams;

@MessagingGateway
public interface UpdateCrawlingTaskEventPublisher {

    @Gateway(requestChannel = CrawlingTaskStreams.UPDATE_TASK_OUTPUT_STREAM)
    void publishEvent(UpdateCrawlingTaskEvent event);
}