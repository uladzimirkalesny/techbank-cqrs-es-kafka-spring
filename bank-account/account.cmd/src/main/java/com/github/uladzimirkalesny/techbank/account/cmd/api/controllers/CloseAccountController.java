package com.github.uladzimirkalesny.techbank.account.cmd.api.controllers;

import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.github.uladzimirkalesny.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.github.uladzimirkalesny.techbank.account.common.dto.BaseResponse;
import com.github.uladzimirkalesny.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j

@RestController
@RequestMapping("/api/v1/closeAccount")
public class CloseAccountController {

    private final CommandDispatcher commandDispatcher;

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable String id) {
        try {
            var closeAccountCommand = new CloseAccountCommand(id);
            commandDispatcher.send(closeAccountCommand);
            return new ResponseEntity<>(
                    new BaseResponse("Bank account closure request completed successfully"),
                    HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e) {
            log.warn("Client made a bad request - {}", e.toString());
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error while processing request to close account with id - {}", id);
            return new ResponseEntity<>(
                    new OpenAccountResponse("Error while processing request to close account with id - " + id, id),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
