package com.github.uladzimirkalesny.techbank.account.cmd.api.commands;

import com.github.uladzimirkalesny.techbank.account.common.dto.AccountType;
import com.github.uladzimirkalesny.techbank.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}
