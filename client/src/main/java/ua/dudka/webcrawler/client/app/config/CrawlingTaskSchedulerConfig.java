package ua.dudka.webcrawler.client.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Configuration
@RequiredArgsConstructor
public class CrawlingTaskSchedulerConfig {

    @Bean
    @Scope("singleton")
    public Map<CrawlingTask, ScheduledFuture> tasksContainer() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ScheduledExecutorService executorService() {
        return Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }
}