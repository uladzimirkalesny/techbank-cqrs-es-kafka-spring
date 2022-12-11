package com.github.uladzimirkalesny.techbank.account.cmd.api.controllers;

import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.github.uladzimirkalesny.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.github.uladzimirkalesny.techbank.account.common.dto.BaseResponse;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j

@RestController
@RequestMapping("/api/v1/openBankAccount")
public class OpenAccountController {
    /**
     * Used to dispatch a given command to a registered command handler method.
     */
    private final CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand openAccountCommand) {
        var id = UUID.randomUUID().toString();
        openAccountCommand.setId(id);
        try {
            commandDispatcher.send(openAccountCommand);
            return new ResponseEntity<>(
                    new OpenAccountResponse("Bank account creation request completed successfully", id),
                    HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            log.warn("Client made a bad request - {}", e.toString());
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error while processing request to open a new bank account for id - {}", id);
            return new ResponseEntity<>(
                    new OpenAccountResponse("Error while processing request to open a new bank account for id - " + id, id),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
