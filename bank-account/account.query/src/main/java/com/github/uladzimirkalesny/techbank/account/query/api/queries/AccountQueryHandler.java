package com.github.uladzimirkalesny.techbank.account.query.api.queries;

import com.github.uladzimirkalesny.techbank.account.query.api.dto.EqualityType;
import com.github.uladzimirkalesny.techbank.account.query.domain.AccountRepository;
import com.github.uladzimirkalesny.techbank.account.query.domain.BankAccount;
import com.github.uladzimirkalesny.techbank.cqrs.core.domain.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor

@Service
public class AccountQueryHandler implements QueryHandler {

    private final AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery findAllAccountsQuery) {
        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery findAccountByIdQuery) {
        var bankAccount = accountRepository.findById(findAccountByIdQuery.getId());
        if (bankAccount.isEmpty()) {
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByAccountHolderQuery findAccountByAccountHolderQuery) {
        var bankAccount = accountRepository.findByAccountHolder(findAccountByAccountHolderQuery.getAccountHolder());
        if (bankAccount.isEmpty()) {
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountsWithBalanceQuery findAccountsWithBalanceQuery) {
        return findAccountsWithBalanceQuery.getEqualityType() == EqualityType.GREATER_THAN
                ? accountRepository.findByBalanceGreaterThan(findAccountsWithBalanceQuery.getBalance())
                : accountRepository.findByBalanceLessThan(findAccountsWithBalanceQuery.getBalance());
    }
}
