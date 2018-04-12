package ua.dudka.webcrawler.client.app.gateway.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ua.dudka.webcrawler.client.app.gateway.ExecuteCrawlingTaskEventSender;
import ua.dudka.webcrawler.client.app.gateway.event.ExecuteCrawlingTaskEvent;

@Component
@Profile("dev")
@Slf4j
public class DummyExecuteCrawlingTaskEventSender implements ExecuteCrawlingTaskEventSender {

    @Override
    public void sendForExecution(ExecuteCrawlingTaskEvent event) {
        log.info("Sending event {} for execution", event);
    }
}