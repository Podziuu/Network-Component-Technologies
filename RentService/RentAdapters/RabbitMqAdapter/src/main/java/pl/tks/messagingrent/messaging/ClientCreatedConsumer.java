package pl.tks.messagingrent.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.tks.messagingrent.event.ClientCreatedEvent;
import pl.tks.modelrent.client.Client;
import pl.tks.servicerent.services.ClientService;

@Component
public class ClientCreatedConsumer {
    private final ClientService clientService;

    public ClientCreatedConsumer(ClientService clientService) {
        this.clientService = clientService;
    }

    @RabbitListener(queues = "${mq.client.create.queue}", containerFactory = "rabbitListenerContainerFactory")
    public void handleClientCreated(ClientCreatedEvent event) {
        System.out.println("Received client: " + event.getFirstName());
        clientService.addClient(new Client(event.getId(), event.getFirstName(), event.getLastName(),
                event.getMaxArticles(), event.getDiscount()));
    }
}

