package ua.dudka.webcrawler.client.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class CrawlingTaskRepositoryTest {

    @Autowired
    private CrawlingTaskRepository repository;


    @Test
    void saveAndDeleteTaskById() {
        CrawlingTask task = new CrawlingTask();
        repository.save(task).block();

        assertThat(repository.findById(task.getId()).block()).isNotNull();

        repository.deleteById(task.getId()).block();

        assertThat(repository.findById(task.getId()).block()).isNull();
    }

    @Test
    void findAllShouldReturnCreatedTasks() {
        List<CrawlingTask> tasks = List.of(
                new CrawlingTask("ss", 1, now().truncatedTo(ChronoUnit.SECONDS)),
                new CrawlingTask("ss", 1, now().truncatedTo(ChronoUnit.SECONDS)),
                new CrawlingTask("ss", 1, now().truncatedTo(ChronoUnit.SECONDS)));
        repository.saveAll(tasks).then().block();

        assertThat(repository.findAll().toStream()
                .allMatch(tasks::contains)).isTrue();
    }
}