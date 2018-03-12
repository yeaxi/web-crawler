package ua.dudka.webcrawler.client.domain.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.web.dto.CreateCrawlingTaskRequest;

public interface CrawlingTaskService {
    Mono<CrawlingTask> addTask(CreateCrawlingTaskRequest request);

    Flux<CrawlingTask> findAll();
}