package ua.dudka.webcrawler.executor.env.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ua.dudka.webcrawler.executor.env.stream.CrawlingTaskStreams;

@Configuration
@Profile("cloud")
@EnableBinding(CrawlingTaskStreams.class)
public class CrawlingTaskStreamConfig {
}