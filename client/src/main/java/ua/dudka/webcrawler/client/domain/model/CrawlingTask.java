package ua.dudka.webcrawler.client.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ua.dudka.webcrawler.client.exception.ReachedMaxVisitedLinksException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Document
@Data
@RequiredArgsConstructor
public class CrawlingTask {

    @Id
    private final String id = UUID.randomUUID().toString();

    private final String startPage;
    private final int maxVisitedLinks;
    private final Set<String> visitedLinks = ConcurrentHashMap.newKeySet();

    private final LocalDateTime startTime;
    private ExecutionStatus executionStatus = ExecutionStatus.SCHEDULED;

    @JsonCreator
    public CrawlingTask(
            @JsonProperty("startPage") String startPage,
            @JsonProperty("maxVisitedLinks") int maxVisitedLinks,
            @JsonProperty("startTime") LocalDateTime startTime,
            @JsonProperty("executionStatus") ExecutionStatus executionStatus) {
        this.startPage = startPage;
        this.maxVisitedLinks = maxVisitedLinks;
        this.startTime = startTime;
        this.executionStatus = executionStatus;
    }

    public CrawlingTask() {
        this.startPage = "";
        this.maxVisitedLinks = 0;
        this.startTime = LocalDateTime.now();
    }

    public void addVisitedLink(String link) {
        if (visitedLinks.size() < maxVisitedLinks) {
            visitedLinks.add(link);
        } else {
            throw new ReachedMaxVisitedLinksException(id, link);
        }
    }
}