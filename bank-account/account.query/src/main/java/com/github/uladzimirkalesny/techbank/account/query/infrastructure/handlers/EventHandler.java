package com.github.uladzimirkalesny.techbank.account.query.infrastructure.handlers;

import com.github.uladzimirkalesny.techbank.account.common.events.AccountClosedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.AccountOpenedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.FundsDepositedEvent;
import com.github.uladzimirkalesny.techbank.account.common.events.FundsWithdrawnEvent;

public interface EventHandler {
    void on(AccountOpenedEvent accountOpenedEvent);

    void on(FundsDepositedEvent fundsDepositedEvent);

    void on(FundsWithdrawnEvent fundsWithdrawnEvent);

    void on(AccountClosedEvent accountClosedEvent);
}
