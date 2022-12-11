package com.github.uladzimirkalesny.techbank.cqrs.core.events;

import com.github.uladzimirkalesny.techbank.cqrs.core.messages.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // includes the base object that extending.
public abstract class BaseEvent extends Message {
    /**
     * The version will become extremely important when replaying the event or to recreating the state of the aggregate.
     * It will also enable to properly implement optimistic concurrency control.
     */
    private int version;
}
