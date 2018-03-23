package ua.dudka.webcrawler.executor.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
public class CrawlingTask {

    private final String id;

    private final StartPage startPage;
    private final LocalDateTime startTime;
    private ExecutionStatus executionStatus;

    @JsonCreator
    public CrawlingTask(
            String id,
            StartPage startPage,
            LocalDateTime startTime,
            ExecutionStatus executionStatus) {
        this.id = id;
        this.startPage = startPage;
        this.startTime = startTime;
        this.executionStatus = executionStatus;
    }

    CrawlingTask() {
        this.id = "";
        this.startPage = StartPage.of("");
        this.startTime = LocalDateTime.now();
    }
}