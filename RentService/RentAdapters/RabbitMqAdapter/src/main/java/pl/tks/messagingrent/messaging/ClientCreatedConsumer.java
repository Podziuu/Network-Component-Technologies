package pl.tks.messagingrent.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.tks.messagingrent.event.ClientCreatedEvent;

@Component
public class ClientCreatedConsumer {

    @RabbitListener(queues = "${mq.client.create.queue}", containerFactory = "rabbitListenerContainerFactory")
    public void handleClientCreated(ClientCreatedEvent event) {
        System.out.println("Received client: " + event.getFirstName());
    }
}

