package com.github.uladzimirkalesny.techbank.account.cmd;

import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.CommandHandler;
import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.RestoreReadDatabaseCommand;
import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor

@SpringBootApplication
public class CommandApplication {

    private final CommandDispatcher commandDispatcher;
    private final CommandHandler commandHandler;

    public static void main(String[] args) {
        SpringApplication.run(CommandApplication.class, args);
    }

    @PostConstruct
    public void registerHandlers() {
        commandDispatcher.registerHandler(OpenAccountCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(DepositFundsCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(WithdrawFundsCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(CloseAccountCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(RestoreReadDatabaseCommand.class, commandHandler::handle);
    }

}
