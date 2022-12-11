package com.github.uladzimirkalesny.techbank.account.cmd.domain;

import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.github.uladzimirkalesny.techbank.account.common.events.AccountClosedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.AccountOpenedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.FundsDepositedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.FundsWithdrawnEvent;
import com.github.uladzimirkalesny.techbank.cqrs.core.domain.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {

    private Boolean active;

    private double balance;

    public AccountAggregate(OpenAccountCommand openAccountCommand) {
        var accountOpenedEvent = AccountOpenedEvent.builder()
                .id(openAccountCommand.getId())
                .accountHolder(openAccountCommand.getAccountHolder())
                .createdDate(new Date())
                .accountType(openAccountCommand.getAccountType())
                .openingBalance(openAccountCommand.getOpeningBalance())
                .build();

        raiseEvent(accountOpenedEvent);
    }

    public void apply(AccountOpenedEvent accountOpenedEvent) {
        this.id = accountOpenedEvent.getId();
        this.active = Boolean.TRUE;
        this.balance = accountOpenedEvent.getOpeningBalance();
    }

    public void depositFunds(double amount) {
        if (!active) {
            throw new IllegalStateException("Funds cannot be deposited into a closed bank account.");
        }
        if (amount <= 0) {
            throw new IllegalStateException("Deposit amount must be grater then 0.");
        }

        var fundsDepositedEvent = FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build();

        raiseEvent(fundsDepositedEvent);
    }

    public void apply(FundsDepositedEvent fundsDepositedEvent) {
        this.id = fundsDepositedEvent.getId();
        this.balance += fundsDepositedEvent.getAmount();
    }

    public void withdrawFunds(double amount) {
        if (!active) {
            throw new IllegalStateException("Funds cannot be withdraw from a closed bank account.");
        }

        var fundsWithdrawnEvent = FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build();

        raiseEvent(fundsWithdrawnEvent);
    }

    public void apply(FundsWithdrawnEvent fundsWithdrawnEvent) {
        this.id = fundsWithdrawnEvent.getId();
        this.balance -= fundsWithdrawnEvent.getAmount();
    }

    public void closeAccount() {
        if (!active) {
            throw new IllegalStateException("The bank account has already been closed");
        }

        var accountClosedEvent = AccountClosedEvent.builder()
                .id(this.id)
                .build();

        raiseEvent(accountClosedEvent);
    }

    public void apply(AccountClosedEvent accountClosedEvent) {
        this.id = accountClosedEvent.getId();
        this.active = Boolean.FALSE;
    }
}
