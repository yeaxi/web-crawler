package ua.dudka.webcrawler.client.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.dudka.webcrawler.client.domain.model.CrawlingTask;
import ua.dudka.webcrawler.client.domain.model.StartPage;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CrawlingTaskRepositoryTest {

    @Autowired
    private CrawlingTaskRepository repository;


    @Test
    public void saveAndDeleteTaskById() {
        CrawlingTask task = new CrawlingTask(StartPage.of("ss"), now());
        repository.save(task).block();

        assertThat(repository.findById(task.getId()).block()).isNotNull();

        repository.deleteById(task.getId()).block();

        assertThat(repository.findById(task.getId()).block()).isNull();
    }

    @Test
    public void findAllShouldReturnCreatedTasks() {
        List<CrawlingTask> tasks = List.of(
                new CrawlingTask(StartPage.of("ss"), now().truncatedTo(ChronoUnit.SECONDS)),
                new CrawlingTask(StartPage.of("ss"), now().truncatedTo(ChronoUnit.SECONDS)),
                new CrawlingTask(StartPage.of("ss"), now().truncatedTo(ChronoUnit.SECONDS)));
        repository.saveAll(tasks).then().block();

        assertThat(repository.findAll().toStream()
                .allMatch(tasks::contains)).isTrue();
    }
}