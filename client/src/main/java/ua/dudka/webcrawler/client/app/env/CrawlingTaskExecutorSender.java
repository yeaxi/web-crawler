package ua.dudka.webcrawler.client.app.env;

import ua.dudka.webcrawler.client.domain.model.CrawlingTask;

public interface CrawlingTaskExecutorSender {
    void sendForExecution(CrawlingTask task);
}
