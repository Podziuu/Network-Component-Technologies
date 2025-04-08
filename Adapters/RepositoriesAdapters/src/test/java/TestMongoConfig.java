import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import repo.MongoEntity;

@TestConfiguration
public class TestMongoConfig {

    @Container
    static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:6.0")
            .withReuse(true)
            .withCommand("--replSet", "rs0")
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "")  // No root user
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "");

    static {
        System.setProperty("TestEnvironment", "true");
        mongoContainer.start();
    }

    @Bean
    @Primary
    public MongoEntity testMongoEntity() {
        return new MongoEntity(mongoContainer.getReplicaSetUrl());
    }
}
