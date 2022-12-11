package com.github.uladzimirkalesny.techbank.account.cmd.api.commands;

import com.github.uladzimirkalesny.techbank.account.cmd.domain.AccountAggregate;
import com.github.uladzimirkalesny.techbank.cqrs.core.handlers.EventSourcingHandlerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class AccountCommandHandler implements CommandHandler {

    private final EventSourcingHandlerHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand openAccountCommand) {
        var accountAggregate = new AccountAggregate(openAccountCommand);
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(DepositFundsCommand depositFundsCommand) {
        var accountAggregate = eventSourcingHandler.getById(depositFundsCommand.getId());
        accountAggregate.depositFunds(depositFundsCommand.getAmount());
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(WithdrawFundsCommand withdrawFundsCommand) {
        var accountAggregate = eventSourcingHandler.getById(withdrawFundsCommand.getId());
        if (withdrawFundsCommand.getAmount() > accountAggregate.getBalance()) {
            throw new IllegalStateException("Withdrawal declined insufficient funds");
        }
        accountAggregate.withdrawFunds(withdrawFundsCommand.getAmount());
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(CloseAccountCommand closeAccountCommand) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(closeAccountCommand.getId());
        accountAggregate.closeAccount();
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(RestoreReadDatabaseCommand restoreReadDatabaseCommand) {
        eventSourcingHandler.republishEvents();
    }
}
