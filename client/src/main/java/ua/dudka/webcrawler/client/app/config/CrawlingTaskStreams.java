package ua.dudka.webcrawler.client.app.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CrawlingTaskStreams {

    String EXECUTE_TASK_OUTPUT_STREAM = "execute_task_stream";
    String UPDATE_TASK_INPUT_STREAM = "update_task_stream";

    @Output(EXECUTE_TASK_OUTPUT_STREAM)
    MessageChannel executeTaskInputStream();

    @Input(UPDATE_TASK_INPUT_STREAM)
    SubscribableChannel updateStatusOutputStream();
}