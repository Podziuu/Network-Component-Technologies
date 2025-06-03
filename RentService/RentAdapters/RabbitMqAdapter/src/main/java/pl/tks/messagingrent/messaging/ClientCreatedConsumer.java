package pl.tks.messagingrent.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.tks.messagingrent.event.ClientCreatedEvent;
import pl.tks.messagingrent.event.RollbackClientEvent;
import pl.tks.modelrent.client.Client;
import pl.tks.servicerent.services.ClientService;

@Component
public class ClientCreatedConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ClientCreatedConsumer.class);

    private final RabbitTemplate rabbitTemplate;

    private final ClientService clientService;

    @Value("${mq.client.rollback.queue:client.rollback.queue}")
    private String rollbackQueue;

    @Autowired
    public ClientCreatedConsumer(ClientService clientService ,RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.clientService = clientService;
    }

    @RabbitListener(queues = "${mq.client.create.queue}")
    public void receiveClientCreatedEvent(ClientCreatedEvent event) {
        try {
            logger.info("Received client creation event: {} {}", event.getFirstName(), event.getLastName());

            if ("ErrorTest".equals(event.getFirstName())) {
                throw new IllegalArgumentException("Simulated error for rollback testing");
            }

            clientService.addClient(new Client(
                    event.getId(),
                    event.getFirstName(),
                    event.getLastName(),
                    event.getMaxArticles(),
                    event.getDiscount()
            ));

        } catch (IllegalArgumentException | IllegalStateException e) {
            handleRollback(event, e);
        } catch (Exception e) {
            logger.error("Unexpected error while processing client creation", e);
            handleRollback(event, e);
        }
    }

    private void handleRollback(ClientCreatedEvent event, Exception e) {
        try {
            if (event.getClientId() == null) {
                logger.warn("Cannot send rollback – client ID is null.");
                return;
            }

            RollbackClientEvent rollbackEvent = new RollbackClientEvent();
            rollbackEvent.setClientId(event.getClientId());
            rollbackEvent.setReason(e.getMessage());

            rabbitTemplate.convertAndSend(rollbackQueue, rollbackEvent);
            logger.info("Sent rollback event for client ID: {}", event.getClientId());
        } catch (Exception ex) {
            logger.error("Failed to send rollback event", ex);
        }
    }
}
