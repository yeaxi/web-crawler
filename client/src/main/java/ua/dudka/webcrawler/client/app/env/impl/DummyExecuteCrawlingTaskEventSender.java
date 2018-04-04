package ua.dudka.webcrawler.client.app.env.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.dudka.webcrawler.client.app.env.ExecuteCrawlingTaskEventSender;
import ua.dudka.webcrawler.client.app.env.event.ExecuteCrawlingTaskEvent;

@Component
@Slf4j
public class DummyExecuteCrawlingTaskEventSender implements ExecuteCrawlingTaskEventSender {

    @Override
    public void sendForExecution(ExecuteCrawlingTaskEvent event) {
        log.info("Sending event {} for execution", event);
    }
}