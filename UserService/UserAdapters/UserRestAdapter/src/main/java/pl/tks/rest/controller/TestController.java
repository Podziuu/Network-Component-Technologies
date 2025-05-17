package pl.tks.rest.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tks.event.ClientCreatedEvent;

@RestController
public class TestController {
    private final RabbitTemplate rabbitTemplate;

    public TestController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/test-event")
    public ResponseEntity<Void> sendTestEvent() {
        ClientCreatedEvent event = new ClientCreatedEvent("123", "Jan", "Kowalski", 2, 0);
        rabbitTemplate.convertAndSend("client.create.queue", event);
        return ResponseEntity.ok().build();
    }
}
