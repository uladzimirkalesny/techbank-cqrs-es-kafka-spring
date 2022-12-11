package com.github.uladzimirkalesny.techbank.account.cmd.api.controllers;

import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.github.uladzimirkalesny.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.github.uladzimirkalesny.techbank.account.common.dto.BaseResponse;
import com.github.uladzimirkalesny.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j

@RestController
@RequestMapping("/api/v1/depositFunds")
public class DepositFundsController {
    private final CommandDispatcher commandDispatcher;

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable String id,
                                                     @RequestBody DepositFundsCommand depositFundsCommand) {
        try {
            depositFundsCommand.setId(id);
            commandDispatcher.send(depositFundsCommand);
            return new ResponseEntity<>(
                    new BaseResponse("Deposit funds request completed successfully"),
                    HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e) {
            log.warn("Client made a bad request - {}", e.toString());
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error while processing request to deposit funds with id - {}", id);
            return new ResponseEntity<>(
                    new OpenAccountResponse("Error while processing request to deposit funds with id - " + id, id),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
