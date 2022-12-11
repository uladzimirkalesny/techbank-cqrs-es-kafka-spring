package com.github.uladzimirkalesny.techbank.account.cmd.api.controllers;

import com.github.uladzimirkalesny.techbank.account.cmd.api.commands.RestoreReadDatabaseCommand;
import com.github.uladzimirkalesny.techbank.account.common.dto.BaseResponse;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Slf4j

@RestController
@RequestMapping("/api/v1/restoreReadDatabase")
public class RestoreReadDatabaseController {
    private final CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> restoreReadDatabase() {
        try {
            commandDispatcher.send(new RestoreReadDatabaseCommand());
            return new ResponseEntity<>(new BaseResponse("Read Database restore request completed successfully"), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            log.warn(MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            var errMsg = "Error while processing request to restore read database";
            log.error(errMsg, e);
            return new ResponseEntity<>(new BaseResponse(errMsg), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
