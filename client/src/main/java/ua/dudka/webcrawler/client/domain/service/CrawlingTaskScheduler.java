package ua.dudka.webcrawler.client.domain.service;

import ua.dudka.webcrawler.client.domain.model.CrawlingTask;

public interface CrawlingTaskScheduler {
    void scheduleExecution(CrawlingTask task);

    void cancelScheduling(CrawlingTask task);
}