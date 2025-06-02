package pl.tks.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.tks.event.ClientCreatedEvent;

@Component
public class ClientProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.client.create.queue}")
    private String clientCreateQueue;

    public ClientProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateClientEvent(ClientCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(clientCreateQueue, event);
            System.out.println("Sent client creation event for: " + event.getFirstName() + " " + event.getLastName());
        } catch (Exception e) {
            // tu tez lapiemy ogolnie po prostu
            System.err.println("Failed to send client creation event: " + e.getMessage());
            throw e;
        }
    }
}
