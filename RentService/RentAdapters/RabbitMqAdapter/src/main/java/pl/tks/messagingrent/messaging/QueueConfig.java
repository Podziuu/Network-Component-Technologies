package pl.tks.messagingrent.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;

@Configuration
public class QueueConfig {

    @Bean
    public Queue clientCreateQueue(@Value("${mq.client.create.queue}") String name) {
        return new Queue(name, true);
    }
}
