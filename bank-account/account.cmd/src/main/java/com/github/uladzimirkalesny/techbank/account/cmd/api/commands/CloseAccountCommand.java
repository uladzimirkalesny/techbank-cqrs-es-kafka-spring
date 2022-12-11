package com.github.uladzimirkalesny.techbank.account.cmd.api.commands;

import com.github.uladzimirkalesny.techbank.cqrs.core.commands.BaseCommand;

public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String id) {
        super(id);
    }
}
