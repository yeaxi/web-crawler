package ua.dudka.webcrawler.client.app.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ua.dudka.webcrawler.client.app.gateway.event.ExecuteCrawlingTaskEvent;

import static ua.dudka.webcrawler.client.app.config.CrawlingTaskStreams.EXECUTE_TASK_OUTPUT_STREAM;

@MessagingGateway
public interface ExecuteCrawlingTaskEventSender {

    @Gateway(requestChannel = EXECUTE_TASK_OUTPUT_STREAM)
    void sendForExecution(ExecuteCrawlingTaskEvent event);
}