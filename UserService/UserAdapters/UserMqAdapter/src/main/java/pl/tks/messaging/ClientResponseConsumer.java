package pl.tks.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.tks.event.RollbackClientEvent;
import pl.tks.ports.infrastructure.RollbackHandlerPort;

@Component
public class ClientResponseConsumer {

    private final RollbackHandlerPort rollbackHandlerPort;

    @Autowired
    public ClientResponseConsumer(RollbackHandlerPort rollbackHandlerPort) {
        this.rollbackHandlerPort = rollbackHandlerPort;
    }

    @RabbitListener(queues = "${mq.client.rollback.queue}")
    public void handleRollbackEvent(RollbackClientEvent event) {
        System.out.println("=== USERSERVICE: Received rollback request ===");
        System.out.println("Client ID: " + event.getClientId());
        System.out.println("Reason: " + event.getReason());

        try {
            rollbackHandlerPort.handleClientCreationRollback(event.getClientId(), event.getReason());
            System.out.println("=== USERSERVICE: Rollback completed successfully ===");
        } catch (Exception e) {
            // łapiemy ogolnie po prostu
            System.err.println("=== USERSERVICE: Error processing rollback ===");
            System.err.println("Error: " + e.getMessage());
        }
    }
}
