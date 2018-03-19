package ua.dudka.webcrawler.client.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCrawlingTaskRequest {
    private String url;
    private LocalDateTime startTime;
    private int maxVisitedLinks;
}