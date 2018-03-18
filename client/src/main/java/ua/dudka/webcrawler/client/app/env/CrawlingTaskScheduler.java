package ua.dudka.webcrawler.client.app.env;

import ua.dudka.webcrawler.client.domain.model.CrawlingTask;

public interface CrawlingTaskScheduler {
    void scheduleExecution(CrawlingTask task);

    void cancelScheduling(CrawlingTask task);
}