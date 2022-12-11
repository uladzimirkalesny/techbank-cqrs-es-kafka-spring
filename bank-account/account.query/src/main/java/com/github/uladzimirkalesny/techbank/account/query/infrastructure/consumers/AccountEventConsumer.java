package com.github.uladzimirkalesny.techbank.account.query.infrastructure.consumers;

import com.github.uladzimirkalesny.techbank.account.common.events.AccountClosedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.AccountOpenedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.FundsDepositedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.FundsWithdrawnEvent;
import com.github.uladzimirkalesny.techbank.account.query.infrastructure.handlers.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class AccountEventConsumer implements EventConsumer {

    private final EventHandler eventHandler;

    @KafkaListener(topics = "AccountOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountOpenedEvent accountOpenedEvent, Acknowledgment acknowledgment) {
        eventHandler.on(accountOpenedEvent);
        acknowledgment.acknowledge(); // commit offset to Kafka
    }

    @KafkaListener(topics = "FundsDepositedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsDepositedEvent fundsDepositedEvent, Acknowledgment acknowledgment) {
        eventHandler.on(fundsDepositedEvent);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "FundsWithdrawnEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsWithdrawnEvent fundsWithdrawnEvent, Acknowledgment acknowledgment) {
        eventHandler.on(fundsWithdrawnEvent);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "AccountClosedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountClosedEvent accountClosedEvent, Acknowledgment acknowledgment) {
        eventHandler.on(accountClosedEvent);
        acknowledgment.acknowledge();
    }
}
