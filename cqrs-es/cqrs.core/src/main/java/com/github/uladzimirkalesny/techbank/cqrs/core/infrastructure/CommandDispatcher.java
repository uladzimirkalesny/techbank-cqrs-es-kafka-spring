package com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure;

import com.github.uladzimirkalesny.techbank.cqrs.core.commands.BaseCommand;
import com.github.uladzimirkalesny.techbank.cqrs.core.commands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);

    void send(BaseCommand command);
}
