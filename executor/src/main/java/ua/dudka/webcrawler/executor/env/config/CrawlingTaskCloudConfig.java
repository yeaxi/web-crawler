package ua.dudka.webcrawler.executor.env.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
@EnableDiscoveryClient
public class CrawlingTaskCloudConfig {
}