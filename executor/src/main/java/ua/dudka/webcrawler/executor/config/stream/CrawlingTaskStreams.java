package ua.dudka.webcrawler.executor.config.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CrawlingTaskStreams {

    String EXECUTE_TASK_INPUT_STREAM = "execute_task_stream";
    String UPDATE_TASK_OUTPUT_STREAM = "update_task_stream";

    @Input(EXECUTE_TASK_INPUT_STREAM)
    SubscribableChannel executeTaskInputStream();

    @Output(UPDATE_TASK_OUTPUT_STREAM)
    MessageChannel updateStatusOutputStream();
}