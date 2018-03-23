package ua.dudka.webcrawler.client.app.env.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.dudka.webcrawler.client.app.env.CrawlingTaskExecutorSender;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;

@Component
@Slf4j
public class DummyCrawlingTaskExecutorSender implements CrawlingTaskExecutorSender {

    @Override
    public void sendForExecution(CrawlingTask task) {
        log.info("Sending task {} for execution", task);
    }
}