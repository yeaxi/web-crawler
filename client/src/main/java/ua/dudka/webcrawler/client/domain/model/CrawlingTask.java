package ua.dudka.webcrawler.client.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;


@Document
@Data
@RequiredArgsConstructor
public class CrawlingTask {

    @Id
    private final String id = UUID.randomUUID().toString();

    private final String startPage;
    private final LocalDateTime startTime;
    private final Duration duration;
    private ExecutionStatus executionStatus = ExecutionStatus.IDLE;

    @JsonCreator
    public CrawlingTask(
            @JsonProperty("startPage") String startPage,
            @JsonProperty("startTime") LocalDateTime startTime,
            @JsonProperty("duration") Duration duration,
            @JsonProperty("executionStatus") ExecutionStatus executionStatus) {
        this.startPage = startPage;
        this.startTime = startTime;
        this.duration = duration;
        this.executionStatus = executionStatus;
    }
}