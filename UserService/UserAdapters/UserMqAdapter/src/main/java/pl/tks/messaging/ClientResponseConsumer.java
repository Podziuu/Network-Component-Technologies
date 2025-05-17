package pl.tks.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.tks.event.RollbackClientEvent;

@Component
public class ClientResponseConsumer {

    @RabbitListener(queues = "${mq.client.rollback.queue}")
    public void handleRollbackClient(RollbackClientEvent event) {
        System.out.println("Received rollback for client ID: " + event.getClientId());
        // Tu dodaj logikę cofnięcia utworzenia klienta po stronie UserService
    }
}

