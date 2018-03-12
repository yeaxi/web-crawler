package ua.dudka.webcrawler.client.app.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
@EnableDiscoveryClient
@EnableBinding
public class CloudConfig {
}
