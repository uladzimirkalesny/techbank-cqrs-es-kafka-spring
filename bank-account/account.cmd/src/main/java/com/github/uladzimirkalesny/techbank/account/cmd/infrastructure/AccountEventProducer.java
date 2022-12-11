package com.github.uladzimirkalesny.techbank.account.cmd.infrastructure;

import com.github.uladzimirkalesny.techbank.cqrs.core.events.BaseEvent;
import com.github.uladzimirkalesny.techbank.cqrs.core.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class AccountEventProducer implements EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void produce(String topicName, BaseEvent event) {
        kafkaTemplate.send(topicName, event);
    }
}
