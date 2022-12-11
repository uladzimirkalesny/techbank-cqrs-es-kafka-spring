package com.github.uladzimirkalesny.techbank.account.cmd.infrastructure;

import com.github.uladzimirkalesny.techbank.account.cmd.domain.AccountAggregate;
import com.github.uladzimirkalesny.techbank.cqrs.core.domain.AggregateRoot;
import com.github.uladzimirkalesny.techbank.cqrs.core.events.BaseEvent;
import com.github.uladzimirkalesny.techbank.cqrs.core.handlers.EventSourcingHandlerHandler;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.EventStore;
import com.github.uladzimirkalesny.techbank.cqrs.core.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@RequiredArgsConstructor

@Service
public class AccountEventSourcingHandler implements EventSourcingHandlerHandler<AccountAggregate> {
    private final EventStore eventStore;

    private final EventProducer eventProducer;

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String aggregateId) {
        var accountAggregate = new AccountAggregate();
        var events = eventStore.getEvents(aggregateId);
        if (events != null && !events.isEmpty()) {
            accountAggregate.replayEvents(events);
            var latestVersion = events.stream()
                    .map(BaseEvent::getVersion)
                    .max(Comparator.naturalOrder());
            accountAggregate.setVersion(latestVersion.get());
        }

        return accountAggregate;
    }

    @Override
    public void republishEvents() {
        var aggregateIds = eventStore.getAggregateIds();
        for (String aggregateId : aggregateIds) {
            var aggregate = getById(aggregateId);
            if (aggregate == null || !aggregate.getActive()) {
                continue;
            }
            var events = eventStore.getEvents(aggregateId);
            for (BaseEvent event : events) {
                eventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }
}
