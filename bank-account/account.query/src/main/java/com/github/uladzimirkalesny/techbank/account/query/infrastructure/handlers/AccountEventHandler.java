package com.github.uladzimirkalesny.techbank.account.query.infrastructure.handlers;

import com.github.uladzimirkalesny.techbank.account.common.events.AccountClosedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.AccountOpenedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.FundsDepositedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.FundsWithdrawnEvent;
import com.github.uladzimirkalesny.techbank.account.query.domain.AccountRepository;
import com.github.uladzimirkalesny.techbank.account.query.domain.BankAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class AccountEventHandler implements EventHandler {

    private final AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent accountOpenedEvent) {
        var bankAccount = BankAccount.builder()
                .id(accountOpenedEvent.getId())
                .accountHolder(accountOpenedEvent.getAccountHolder())
                .accountType(accountOpenedEvent.getAccountType())
                .creationDate(accountOpenedEvent.getCreatedDate())
                .balance(accountOpenedEvent.getOpeningBalance())
                .build();

        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositedEvent fundsDepositedEvent) {
        var bankAccount = accountRepository.findById(fundsDepositedEvent.getId());
        if (bankAccount.isEmpty()) {
            return;
        }

        var currentBalance = bankAccount.get().getBalance();
        var latestBalance = currentBalance + fundsDepositedEvent.getAmount();

        bankAccount.get().setBalance(latestBalance);

        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(FundsWithdrawnEvent fundsWithdrawnEvent) {
        var bankAccount = accountRepository.findById(fundsWithdrawnEvent.getId());
        if (bankAccount.isEmpty()) {
            return;
        }

        var currentBalance = bankAccount.get().getBalance();
        var latestBalance = currentBalance - fundsWithdrawnEvent.getAmount();

        bankAccount.get().setBalance(latestBalance);

        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(AccountClosedEvent accountClosedEvent) {
        accountRepository.deleteById(accountClosedEvent.getId());
    }
}
