package ua.dudka.webcrawler.client.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;

public interface CrawlingTaskRepository extends ReactiveMongoRepository<CrawlingTask, String> {
}