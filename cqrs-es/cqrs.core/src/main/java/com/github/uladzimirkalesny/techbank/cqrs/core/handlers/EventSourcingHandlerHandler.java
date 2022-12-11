package com.github.uladzimirkalesny.techbank.cqrs.core.handlers;

import com.github.uladzimirkalesny.techbank.cqrs.core.domain.AggregateRoot;

public interface EventSourcingHandlerHandler<T> {
    void save(AggregateRoot aggregate);

    T getById(String aggregateId);

    void republishEvents();
}
