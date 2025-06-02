package pl.tks.configmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${mq.client.create.queue:client.create.queue}")
    private String clientCreateQueue;

    @Value("${mq.client.rollback.queue:client-rollback-queue}")
    private String clientRollbackQueue;

    @Bean
    public Queue clientCreateQueue() {
        return QueueBuilder.durable(clientCreateQueue).build();
    }

    @Bean
    public Queue clientRollbackQueue() {
        return QueueBuilder.durable(clientRollbackQueue).build();
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
