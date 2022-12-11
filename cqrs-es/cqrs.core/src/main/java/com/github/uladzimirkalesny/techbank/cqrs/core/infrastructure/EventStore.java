package com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure;

import com.github.uladzimirkalesny.techbank.cqrs.core.events.BaseEvent;

import java.util.List;

public interface EventStore {
    void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion);

    List<BaseEvent> getEvents(String aggregateId);

    List<String> getAggregateIds();
}
