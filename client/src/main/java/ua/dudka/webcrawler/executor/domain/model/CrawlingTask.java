package ua.dudka.webcrawler.executor.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ua.dudka.webcrawler.executor.domain.model.vo.Identifier;
import ua.dudka.webcrawler.executor.domain.model.vo.StartPage;

import java.time.Duration;
import java.time.LocalDateTime;


@Document
@Data
@RequiredArgsConstructor
public class CrawlingTask {

    @Id
    private final Identifier id = Identifier.randomUUID();

    private final StartPage startPage;
    private final LocalDateTime startTime;
    private final Duration duration;
    private ExecutionStatus executionStatus = ExecutionStatus.IDLE;
}