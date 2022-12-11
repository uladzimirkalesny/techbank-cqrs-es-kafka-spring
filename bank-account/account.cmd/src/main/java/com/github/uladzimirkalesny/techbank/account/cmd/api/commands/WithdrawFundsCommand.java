package com.github.uladzimirkalesny.techbank.account.cmd.api.commands;

import com.github.uladzimirkalesny.techbank.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class WithdrawFundsCommand extends BaseCommand {
    private double amount;
}
