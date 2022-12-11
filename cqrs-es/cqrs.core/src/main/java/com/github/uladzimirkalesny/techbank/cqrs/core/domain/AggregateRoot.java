package com.github.uladzimirkalesny.techbank.cqrs.core.domain;

import com.github.uladzimirkalesny.techbank.cqrs.core.events.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AggregateRoot {
    @Getter
    protected String id;

    @Getter
    @Setter
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();

    /**
     * Returns list of changes that are made to the aggregate.
     *
     * @return uncommitted changes
     */
    public List<BaseEvent> getUncommittedChanges() {
        return changes;
    }

    /**
     * Commit all changes that are made to the aggregate.
     */
    public void markChangesAsCommitted() {
        changes.clear();
    }

    /**
     * Applies changes for aggregate.
     *
     * @param event      event for aggregate
     * @param isNewEvent specify is new event or not
     */
    protected void applyChanges(BaseEvent event, Boolean isNewEvent) {
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException e) {
            log.warn("The apply method was not found in the aggregate for {}", event.getClass().getName());
        } catch (Exception e) {
            log.error("Error applying event to aggregate.", e);
        } finally {
            if (isNewEvent) {
                changes.add(event);
            }
        }
    }

    /**
     * Specifies that new event that should be raised.
     * Will not reset those two lists of changes because those changes have already been applied.
     *
     * @param event new event
     */
    public void raiseEvent(BaseEvent event) {
        applyChanges(event, true);
    }

    /**
     * Old events which using to recreate the state of aggregate.
     * Will not reset those two lists of changes because those changes have already been applied.
     *
     * @param events collection of old events
     */
    public void replayEvents(Iterable<BaseEvent> events) {
        events.forEach(event -> applyChanges(event, false));
    }
}
