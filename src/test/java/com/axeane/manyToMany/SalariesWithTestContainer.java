package com.axeane.manyToMany;

import com.axeane.manyToMany.model.Salarie;
import com.axeane.manyToMany.repositories.SalariesRepository;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Duration;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {SalariesWithTestContainer.Initializer.class})
@SpringBootTest(classes = ManyToManyApplication.class)
@TestPropertySource("/application-test.properties")
public class SalariesWithTestContainer {

    @Autowired
    private SalariesRepository salariesRepository;
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres:9.6.10")
                    .withDatabaseName(" spring")
                    .withUsername("postgres")
                    .withPassword("root")
                    .withStartupTimeout(Duration.ofSeconds(10));

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    public void testWithDbs() {
        Salarie salarie = new Salarie("ilyes", "raissi", "Tunis");
        Salarie salarie1 = new Salarie("rahma", "raissi", "Tunis");
        salariesRepository.save(salarie);
        salariesRepository.save(salarie1);

        assertThat(salarie)
                .matches(c -> Objects.equals(c.getNom(), "ilyes") && Objects.equals(c.getPrenom(), "raissi") && Objects.equals(c.getAdresse(), "Tunis"));

        assertThat(salarie1)
                .matches(c -> Objects.equals(c.getNom(), "rahma") && Objects.equals(c.getPrenom(), "raissi") && Objects.equals(c.getAdresse(), "Tunis"));
    }
}
