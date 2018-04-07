package ua.dudka.webcrawler.executor.app.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("stream")
@EnableBinding(CrawlingTaskStreams.class)
public class CrawlingTaskStreamConfig {
}