package com.github.uladzimirkalesny.techbank.cqrs.core.producers;

import com.github.uladzimirkalesny.techbank.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void produce(String topicName, BaseEvent event);
}
