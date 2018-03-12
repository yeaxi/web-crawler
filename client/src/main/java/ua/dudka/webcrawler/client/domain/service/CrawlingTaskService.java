package ua.dudka.webcrawler.client.domain.service;

import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.web.request.CreateCrawlingTaskRequest;

public interface CrawlingTaskService {
    Mono<CrawlingTask> addCrawlingTask(CreateCrawlingTaskRequest request);
}