package pl.tks.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.tks.event.ClientCreatedEvent;

@Component
public class ClientProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${mq.client.create.queue}")
    private String createClientQueue;

    public ClientProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendCreateClientEvent(ClientCreatedEvent event) {
        rabbitTemplate.convertAndSend(createClientQueue, event);
    }
}
