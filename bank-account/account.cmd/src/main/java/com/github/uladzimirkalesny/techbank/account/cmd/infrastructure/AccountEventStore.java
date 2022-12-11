package com.github.uladzimirkalesny.techbank.account.cmd.infrastructure;

import com.github.uladzimirkalesny.techbank.account.cmd.domain.AccountAggregate;
import com.github.uladzimirkalesny.techbank.account.cmd.domain.EventStoreRepository;
import com.github.uladzimirkalesny.techbank.cqrs.core.events.BaseEvent;
import com.github.uladzimirkalesny.techbank.cqrs.core.events.EventModel;
import com.github.uladzimirkalesny.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.github.uladzimirkalesny.techbank.cqrs.core.exception.ConcurrencyException;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.EventStore;
import com.github.uladzimirkalesny.techbank.cqrs.core.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor

@Service
public class AccountEventStore implements EventStore {

    private final EventProducer accountEventProducer;
    private final EventStoreRepository eventStoreRepository;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        List<EventModel> aggregateEvents = eventStoreRepository.findByAggregateIdentifier(aggregateId);

        if (expectedVersion != -1 && aggregateEvents.get(aggregateEvents.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }

        var version = expectedVersion;
        for (BaseEvent event : events) {
            version++;

            event.setVersion(version);

            var eventModel = EventModel.builder()
                    .timestamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();

            var persistedEvent = eventStoreRepository.save(eventModel);
            if (!persistedEvent.getId().isEmpty()) {
                accountEventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        List<EventModel> eventModels = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (eventModels == null || eventModels.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account id provided");
        }

        return eventModels.stream()
                .map(EventModel::getEventData)
                .toList();
    }

    @Override
    public List<String> getAggregateIds() {
        var eventModels = eventStoreRepository.findAll();
        if (eventModels.isEmpty()) {
            throw  new IllegalStateException("Could not retrieve events from the event store");
        }
        return eventModels.stream()
                .map(EventModel::getAggregateIdentifier)
                .distinct()
                .toList();
    }
}
