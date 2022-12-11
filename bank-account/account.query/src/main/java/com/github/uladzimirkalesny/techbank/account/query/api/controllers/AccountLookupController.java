package com.github.uladzimirkalesny.techbank.account.query.api.controllers;

import com.github.uladzimirkalesny.techbank.account.query.api.dto.AccountLookupResponse;
import com.github.uladzimirkalesny.techbank.account.query.api.dto.EqualityType;
import com.github.uladzimirkalesny.techbank.account.query.api.queries.FindAccountByAccountHolderQuery;
import com.github.uladzimirkalesny.techbank.account.query.api.queries.FindAccountByIdQuery;
import com.github.uladzimirkalesny.techbank.account.query.api.queries.FindAccountsWithBalanceQuery;
import com.github.uladzimirkalesny.techbank.account.query.api.queries.FindAllAccountsQuery;
import com.github.uladzimirkalesny.techbank.account.query.domain.BankAccount;
import com.github.uladzimirkalesny.techbank.cqrs.core.infrastructure.QueryDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;

@RequiredArgsConstructor
@Slf4j

@RestController
@RequestMapping("/api/v1/bankAccountLookup")
public class AccountLookupController {

    private final QueryDispatcher queryDispatcher;

    @GetMapping
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        try {
            List<BankAccount> accountList = queryDispatcher.send(new FindAllAccountsQuery());
            if (accountList == null || accountList.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .accounts(accountList)
                    .message(MessageFormat.format("Successfully returned {0} bank accounts", accountList.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var errMsg = "Failed to complete get all accounts request";
            log.error(errMsg);
            return new ResponseEntity<>(new AccountLookupResponse(errMsg), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable String id) {
        try {
            List<BankAccount> accountList = queryDispatcher.send(new FindAccountByIdQuery(id));
            if (accountList == null || accountList.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .accounts(accountList)
                    .message("Successfully returned bank account")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var errMsg = "Failed to complete get account by id request";
            log.error(errMsg);
            return new ResponseEntity<>(new AccountLookupResponse(errMsg), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byAccountHolder/{accountHolder}")
    public ResponseEntity<AccountLookupResponse> getAccountByAccountHolder(@PathVariable String accountHolder) {
        try {
            List<BankAccount> accountList = queryDispatcher.send(new FindAccountByAccountHolderQuery(accountHolder));
            if (accountList == null || accountList.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .accounts(accountList)
                    .message("Successfully returned bank account")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var errMsg = "Failed to complete get account by account holder request";
            log.error(errMsg);
            return new ResponseEntity<>(new AccountLookupResponse(errMsg), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountWithBalance(@PathVariable EqualityType equalityType,
                                                                       @PathVariable double balance) {
        try {
            var findAccountsWithBalanceQuery = new FindAccountsWithBalanceQuery(equalityType, balance);
            List<BankAccount> accountList = queryDispatcher.send(findAccountsWithBalanceQuery);
            if (accountList == null || accountList.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .accounts(accountList)
                    .message(MessageFormat.format("Successfully returned {0} bank accounts", accountList.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var errMsg = "Failed to complete get account with balance request";
            log.error(errMsg);
            return new ResponseEntity<>(new AccountLookupResponse(errMsg), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
